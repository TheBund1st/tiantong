package com.thebund1st.tiantong.core

class DummyProviderSpecificRequest implements ProviderSpecificRequest {
    private String dummyId

    String getDummyId() {
        return dummyId
    }

    void setDummyId(String dummyId) {
        this.dummyId = dummyId
    }
}