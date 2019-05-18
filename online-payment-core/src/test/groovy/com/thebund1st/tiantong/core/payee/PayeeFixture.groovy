package com.thebund1st.tiantong.core.payee


import com.thebund1st.tiantong.core.payee.Payee
import com.thebund1st.tiantong.utils.Randoms

class PayeeFixture {
    private String context
    private String partyId

    def withContext(String value) {
        this.context = value
        this
    }

    def withPartyId(String value) {
        this.partyId = value
        this
    }

    def build() {
        Payee.of(context, partyId)
    }

    static def aStore() {
        new PayeeFixture()
                .withContext("STORE")
                .withPartyId(Randoms.randomStr())
    }
}
