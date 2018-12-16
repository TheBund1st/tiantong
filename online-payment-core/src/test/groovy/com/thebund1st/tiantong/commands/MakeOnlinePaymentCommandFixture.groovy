package com.thebund1st.tiantong.commands

class MakeOnlinePaymentCommandFixture {
    private MakeOnlinePaymentCommand target = new MakeOnlinePaymentCommand()

    def amountIs(double amount) {
        target.setAmount(amount)
        this
    }

    def build() {
        target
    }

    static def aMakeOnlinePaymentCommand() {
        new MakeOnlinePaymentCommandFixture()
                .amountIs(100.00)
    }

}
