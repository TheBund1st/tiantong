package com.thebund1st.tiantong

import com.thebund1st.tiantong.core.payment.ProviderSpecificCloseOnlinePaymentGateway
import com.thebund1st.tiantong.core.payment.ProviderSpecificCreateOnlinePaymentGateway
import com.thebund1st.tiantong.core.payment.ProviderSpecificCreateOnlinePaymentRequest
import com.thebund1st.tiantong.core.payment.ProviderSpecificLaunchOnlinePaymentRequest
import com.thebund1st.tiantong.core.payment.ProviderSpecificPullOnlinePaymentResultGateway
import com.thebund1st.tiantong.json.deserializers.MethodBasedProviderSpecificCreateOnlinePaymentRequestJsonDeserializer
import com.thebund1st.tiantong.web.webhooks.NotifyOnlinePaymentResultCommandAssembler
import com.thebund1st.tiantong.web.webhooks.NotifyOnlinePaymentResultResponseBodyAssembler
import com.thebund1st.tiantong.wechatpay.payment.WeChatPayCreateOnlinePaymentRequestWxPayUnifiedOrderRequestPopulator
import com.thebund1st.tiantong.wechatpay.payment.WeChatPayLaunchOnlinePaymentRequestAssembler
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.ArchRule
import spock.lang.Specification
import spock.lang.Unroll

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses

class PortsAndAdaptersTest extends Specification {
    private JavaClasses importedClasses = new ClassFileImporter()
            .importPackages("com.thebund1st.tiantong")

    @Unroll("#rule")
    def "Dependency Rules"(def rule, ArchRule ruleCheck) {
        expect:
        ruleCheck.check(this.importedClasses)

        where:
        rule                                        | ruleCheck
        "application can not be accessed by domain" | noClasses()
                .that().resideInAPackage("com.thebund1st.tiantong.core")
                .should().accessClassesThat().resideInAnyPackage("com.thebund1st.tiantong.application")
    }

    @Unroll("#rule")
    def "Domain Implementations Rules"(def rule, ArchRule ruleCheck) {
        expect:
        ruleCheck.check(this.importedClasses)

        where:
        rule                                                                                                             | ruleCheck
        "${ProviderSpecificCreateOnlinePaymentRequest.simpleName} implementations should be named with universal suffix" | classes()
                .that().implement(ProviderSpecificCreateOnlinePaymentRequest)
                .should().haveSimpleNameEndingWith("CreateOnlinePaymentRequest")
        "${ProviderSpecificLaunchOnlinePaymentRequest.simpleName} implementations should be named with universal suffix" | classes()
                .that().implement(ProviderSpecificLaunchOnlinePaymentRequest)
                .should().haveSimpleNameEndingWith("LaunchOnlinePaymentRequest")
        "${ProviderSpecificCreateOnlinePaymentGateway.simpleName} implementations should be named with universal suffix"     | classes()
                .that().implement(ProviderSpecificCreateOnlinePaymentGateway)
                .should().haveSimpleNameEndingWith("CreateOnlinePaymentGateway")
                .orShould().haveSimpleName(ProviderSpecificCreateOnlinePaymentGateway.simpleName + "Dispatcher")
                .orShould().haveSimpleNameEndingWith("OnlinePaymentGateway")
        "${ProviderSpecificCloseOnlinePaymentGateway.simpleName} implementations should be named with universal suffix"      | classes()
                .that().implement(ProviderSpecificCloseOnlinePaymentGateway)
                .should().haveSimpleNameEndingWith("CloseOnlinePaymentGateway")
                .orShould().haveSimpleName(ProviderSpecificCloseOnlinePaymentGateway.simpleName + "Dispatcher")
                .orShould().haveSimpleNameEndingWith("OnlinePaymentGateway")
        "${ProviderSpecificPullOnlinePaymentResultGateway.simpleName} implementations should be named with universal suffix" | classes()
                .that().implement(ProviderSpecificPullOnlinePaymentResultGateway)
                .should().haveSimpleNameEndingWith("PullOnlinePaymentResultGateway")
                .orShould().haveSimpleName(ProviderSpecificPullOnlinePaymentResultGateway.simpleName + "Dispatcher")
                .orShould().haveSimpleNameEndingWith("OnlinePaymentGateway")
    }

    @Unroll("#rule")
    def "Json Implementations Rules"(def rule, ArchRule ruleCheck) {
        expect:
        ruleCheck.check(this.importedClasses)

        where:
        rule                                                                                                                                        | ruleCheck
        "${MethodBasedProviderSpecificCreateOnlinePaymentRequestJsonDeserializer.simpleName} implementations should be named with universal suffix" | classes()
                .that().areAssignableTo(MethodBasedProviderSpecificCreateOnlinePaymentRequestJsonDeserializer)
                .should().haveSimpleNameEndingWith("CreateOnlinePaymentRequestJsonDeserializer")
        "json serializers should be named with universal suffix"                                                                                    | classes()
                .that().resideInAPackage("com.thebud1st.tiantong.json.serializers")
                .should().haveSimpleNameEndingWith("JsonSerializer")
        "json deserializers should be named with universal suffix"                                                                                  | classes()
                .that().resideInAPackage("com.thebud1st.tiantong.json.deserializers")
                .should().haveSimpleNameEndingWith("JsonDeserializer")
    }

    @Unroll("#rule")
    def "Webhook Implementations Rules"(def rule, ArchRule ruleCheck) {
        expect:
        ruleCheck.check(this.importedClasses)

        where:
        rule                                                                                                                 | ruleCheck
        "${NotifyOnlinePaymentResultCommandAssembler.simpleName} implementations should be named with universal suffix"      | classes()
                .that().areAssignableTo(NotifyOnlinePaymentResultCommandAssembler)
                .should().haveSimpleNameEndingWith("NotifyOnlinePaymentResultCommandAssembler")
        "${NotifyOnlinePaymentResultResponseBodyAssembler.simpleName} implementations should be named with universal suffix" | classes()
                .that().areAssignableTo(NotifyOnlinePaymentResultResponseBodyAssembler)
                .should().haveSimpleNameEndingWith("NotifyOnlinePaymentResultResponseBodyAssembler")
    }

    @Unroll("#rule")
    def "WeChatPay Implementations Rules"(def rule, ArchRule ruleCheck) {
        expect:
        ruleCheck.check(this.importedClasses)

        where:
        rule                                                                                                                                       | ruleCheck
        "${WeChatPayLaunchOnlinePaymentRequestAssembler.simpleName} implementations should be named with universal suffix"                         | classes()
                .that().implement(WeChatPayLaunchOnlinePaymentRequestAssembler)
                .and().haveSimpleNameNotEndingWith("Dispatcher")
                .should().haveSimpleNameEndingWith("LaunchOnlinePaymentRequestAssembler")
        "${WeChatPayCreateOnlinePaymentRequestWxPayUnifiedOrderRequestPopulator.simpleName} implementations should be named with universal suffix" | classes()
                .that().implement(WeChatPayCreateOnlinePaymentRequestWxPayUnifiedOrderRequestPopulator)
                .and().haveSimpleNameNotEndingWith("Dispatcher")
                .should().haveSimpleNameEndingWith("CreateOnlinePaymentRequestWxPayUnifiedOrderRequestPopulator")
    }
}
