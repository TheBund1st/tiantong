package foo.bar.steps

class RequestOnlinePayment {
    void "enters username and password"() {
        ppManager.login()
        emUser.login()
    }

    void "logged in"() {
        ppManager.currentLoggedInUser()
        emUser.currentLoggedInUser()
    }

}
