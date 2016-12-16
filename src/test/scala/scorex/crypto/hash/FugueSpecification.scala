package scorex.crypto.hash

import scorex.utils.BytesHex.hex2bytes

class FugueSpecification extends HashTest {

  hashCheck(Fugue256,
    Map(
      emptyBytes -> "D6EC528980C130AAD1D1ACD28B9DD8DBDEAE0D79EDED1FCA72C2AF9F37C2246F",
      hex2bytes("cc") -> "B894EB2DF58162F6C48D495F156E73BD086DD13DB407EE38781177BB23D129BB"
    )
  )

  hashCheck(Fugue512,
    Map(
      emptyBytes -> "3124F0CBB5A1C2FB3CE747ADA63ED2AB3BCD74795CEF2B0E805D5319FCC360B4617B6A7EB631D66F6D106ED0724B56FA8C1110F9B8DF1C6898E7CA3C2DFCCF79",
      hex2bytes("cc") -> "2EF4115479B060FC64A4D6F6913A39E326AFC81DEB4E39D71C573DF5ED132200E7C784BAB1804930CAD16847F16CBDA59A865BBD928EBC17D33689FEF233C10B"
    )
  )

}
