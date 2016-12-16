package scorex.crypto.hash

import scorex.utils.BytesHex.hex2bytes

class EchoSpecification extends HashTest {

  hashCheck(ECHO256,
    Map(
      emptyBytes -> "4496CD09D425999AEFA75189EE7FD3C97362AA9E4CA898328002D20A4B519788",
      hex2bytes("724627916C50338643E6996F07877EAFD96BDF01DA7E991D4155B9BE1295EA7D21C9391F4C4A41C75F77E5D27389253393725F1427F57914B273AB862B9E31DABCE506E558720520D33352D119F699E784F9E548FF91BC35CA147042128709820D69A8287EA3257857615EB0321270E94B84F446942765CE882B191FAEE7E1C87E0F0BD4E0CD8A927703524B559B769CA4ECE1F6DBF313FDCF67C572EC4185C1A88E86EC11B6454B371980020F19633B6B95BD280E4FBCB0161E1A82470320CEC6ECFA25AC73D09F1536F286D3F9DACAFB2CD1D0CE72D64D197F5C7520B3CCB2FD74EB72664BA93853EF41EABF52F015DD591500D018DD162815CC993595B195") -> "43513DDCC5811D4DC7EA29677A1647FE0E9E0C186F8B33EEBF4CF94A78183429",
      hex2bytes("cc") -> "01c382b5b9d7d10ec36c98785c27eaccfb2f772a7e58b6b97bf62212b8584ae5"
    )
  )

  hashCheck(ECHO512,
    Map(
      emptyBytes-> "158F58CC79D300A9AA292515049275D051A28AB931726D0EC44BDD9FAEF4A702C36DB9E7922FFF077402236465833C5CC76AF4EFC352B4B44C7FA15AA0EF234E",
      hex2bytes("724627916C50338643E6996F07877EAFD96BDF01DA7E991D4155B9BE1295EA7D21C9391F4C4A41C75F77E5D27389253393725F1427F57914B273AB862B9E31DABCE506E558720520D33352D119F699E784F9E548FF91BC35CA147042128709820D69A8287EA3257857615EB0321270E94B84F446942765CE882B191FAEE7E1C87E0F0BD4E0CD8A927703524B559B769CA4ECE1F6DBF313FDCF67C572EC4185C1A88E86EC11B6454B371980020F19633B6B95BD280E4FBCB0161E1A82470320CEC6ECFA25AC73D09F1536F286D3F9DACAFB2CD1D0CE72D64D197F5C7520B3CCB2FD74EB72664BA93853EF41EABF52F015DD591500D018DD162815CC993595B195") -> "54E5A3DD3FBDD5DB68CCEB628CEFA4458B932650D3B62C2717CCED8F37338C409F0658BE58425B7DFE70E29C0031D9E24CD685FB8DD5B2DB2D4E4C7C3F98B2E9",
      hex2bytes("cc") -> "DFCE37CA6F32BA4C3A72E77BCA20E511A39B31A6075815F083DB2ECFD5C32CFD6A4E0DD9BD51921199758EDD2FE8ED0FA31E06AA821C7030653D15408E8728DD"
    )
  )

}
