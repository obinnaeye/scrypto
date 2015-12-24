package scorex.crypto.hash

import scorex.crypto._

class SIMDSpecification extends HashTest {

  hashCheck(SIMD256,
    Map(
      emptyBytes -> "8029E81E7320E13ED9001DC3D8021FEC695B7A25CD43AD805260181C35FCAEA8",
      hex2bytes("cc") -> "4ACB11B332C3CB462B60EBBB0DEC32EF7A2A3470AF49EC5C10AA52A484A640D4"
    )
  )

  hashCheck(SIMD512,
    Map(
      emptyBytes -> "51A5AF7E243CD9A5989F7792C880C4C3168C3D60C4518725FE5757D1F7A69C6366977EABA7905CE2DA5D7CFD07773725F0935B55F3EFB954996689A49B6D29E0",
      hex2bytes("cc") -> "6FD2D5E6104BD3966283321234CD40F4ED380CB53A03911B610746466C10A93E41C9B745C79DFDE3275980FE82FC8372EFC406A9B0BDC8C63A375954E63436E2"
    )
  )

}
