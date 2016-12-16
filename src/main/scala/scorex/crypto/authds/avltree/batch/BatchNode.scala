package scorex.crypto.authds.avltree.batch

import scorex.crypto.authds.TwoPartyDictionary.Label
import scorex.crypto.encode.Base58
import scorex.crypto.hash.ThreadUnsafeHash
import scala.collection.mutable

sealed trait Node {
  var visited: Boolean = false // TODO: this puts "visited" even into Verifier nodes, which means the verifier has a wasted Boolean in every node, because the verifier does not care about visited. An alternative is to define setVisited as a method in prover nodes (where it will set visited = true) and verifier nodes (where it will do nothing), and have the code call this method instead of simply writing visited=true. However, the overhead of the method call that depends on the node type is probably not worth the few extra bytes we will save, because the verifier will not typically have a lot of nodes

  protected def computeLabel: Label

  protected def arrayToString(a: Array[Byte]): String = Base58.encode(a).take(8) // TODO: why is this needed?
  protected var labelOpt: Option[Label] = None

  def label: Label = labelOpt match {
    case None =>
      val l = computeLabel
      labelOpt = Some(l)
      l
    case Some(l) =>
      l
  }
}

sealed trait ProverNodes extends Node {
  var isNew: Boolean = true
  protected var k: AVLKey
  def key = k
  
  // TODO: Debug only
  var height: Int
}
sealed trait VerifierNodes extends Node

class LabelOnlyNode(l: Label) extends VerifierNodes {
  labelOpt = Some(l)
  protected def computeLabel: Label = l
}

sealed trait InternalNode extends Node {
  protected var l: Node
  protected var r: Node
  protected var b: Balance
  
  protected val hf: ThreadUnsafeHash
  protected def computeLabel: Label = hf.prefixedHash(1: Byte, Array(b), l.label, r.label) // TODO: why Array(balance)?

  def balance: Balance = b
  def left: Node = l
  def right: Node = r
  
  /* These two method may either mutate the existing node or create a new one */
  def getNew (newLeft: Node = l, newRight: Node = r, newBalance: Balance = b): InternalNode
  def getNewKey(newKey: AVLKey) : InternalNode
}

class InternalProverNode(protected var k: AVLKey, protected var l: Node, protected var r: Node,
                      protected var b: Balance = 0)(implicit val hf: ThreadUnsafeHash)
  extends ProverNodes with InternalNode {
  
  
  /* This method will mutate the existing node if isNew = true; else create a new one */
  def getNewKey(newKey: AVLKey) : InternalProverNode = {
    if (isNew) {
      k = newKey // label doesn't change when key of an internal node changes
      this
    } else {
      val ret = new InternalProverNode (newKey, l, r, b)
      ret.labelOpt = labelOpt // label doesn't change when key of an internal node changes
      ret
    }
  }
  
  /* This method will mutate the existing node if isNew = true; else create a new one */
  def getNew(newLeft: Node = l, newRight: Node = r, newBalance: Balance = b): InternalProverNode = {
    if (isNew) {
      l = newLeft
      r = newRight
      b = newBalance
      assert(checkHeight)
      labelOpt = None
      this
    } else {
      val ret = new InternalProverNode(k, newLeft, newRight, newBalance)
      assert(ret.checkHeight)
      ret
    }
    
  }

  // TODO needed for debug only
  var height = 1
  private def checkHeight: Boolean = {
    val lh = left.asInstanceOf[ProverNodes].height
    val rh = right.asInstanceOf[ProverNodes].height
    
    height = math.max(lh, rh) + 1
    balance == rh-lh && balance >= -1 && balance <= 1
    true
  }

  override def toString: String = {
    s"${arrayToString(label)}: ProverNode(${arrayToString(key)}, ${arrayToString(left.label)}, " +
      s"${arrayToString(right.label)}, $balance)" // TODO: Fix in node.java
  }
}

class InternalVerifierNode(protected var l: Node, protected var r: Node, protected var b: Balance)
                       (implicit val hf: ThreadUnsafeHash) extends VerifierNodes with InternalNode {


  def getNewKey(newKey: AVLKey) : InternalNode = {this} // Itnernal Verifier Keys have no keys -- so no-op
  
  /* This method will mutate the existing node if isNew = true; else create a new one */
  def getNew(newLeft: Node = l, newRight: Node = r, newBalance: Balance = b): InternalVerifierNode = {
    l = newLeft
    r = newRight
    b = newBalance
    labelOpt = None
    this
  }

  override def toString: String = {
    s"${arrayToString(label)}: VerifierNode(${arrayToString(left.label)}, ${arrayToString(right.label)}, $balance)"
  }
}

sealed trait Leaf extends Node {
  protected var k: AVLKey
  protected var nk: AVLKey
  protected var v: AVLValue

  def key: AVLKey = k
  def nextLeafKey: AVLKey = nk
  def value: AVLValue = v
  
  protected val hf: ThreadUnsafeHash // TODO: Seems very wasteful to store hf in every node of the tree, when they are all the same. Is there a better way? Pass them in to label method from above? Same for InternalNode and for other, non-batch, trees

  protected def computeLabel: Label = hf.prefixedHash(0: Byte, k, v, nk)

  def getNew (newKey: AVLKey = k, newValue: AVLValue = v, newNextLeafKey : AVLKey = nk): Leaf

  override def toString: String = {
    s"${arrayToString(label)}: Leaf(${arrayToString(key)}, ${arrayToString(value)}, ${arrayToString(nextLeafKey)})"
  }
}

class VerifierLeaf(protected var k: AVLKey, protected var v: AVLValue, protected var nk: AVLKey)
               (implicit val hf: ThreadUnsafeHash) extends Leaf with VerifierNodes{

  /* This method will mutate the existing node if isNew = true; else create a new one */
  def getNew(newKey: AVLKey = k, newValue: AVLValue = v, newNextLeafKey : AVLKey = nk): VerifierLeaf = {
    k = newKey
    v = newValue
    nk = newNextLeafKey
    labelOpt = None
    this
  }
}
  
class ProverLeaf (protected var k: AVLKey, protected var v: AVLValue, protected var nk: AVLKey)
               (implicit val hf: ThreadUnsafeHash) extends Leaf with ProverNodes {

  override def key = k // TODO: we inherit definition of key from two places -- is this the right way to handle it?
  
  /* This method will mutate the existing node if isNew = true; else create a new one */
  def getNew(newKey: AVLKey = k, newValue: AVLValue = v, newNextLeafKey : AVLKey = nk): ProverLeaf = {
    if (isNew) {
      k = newKey
      v = newValue
      nk = newNextLeafKey
      labelOpt = None
      this
    } else {
      new ProverLeaf(newKey, newValue, newNextLeafKey)
    }
  }
  var height = 0 // TODO: needed for debug only
}


