package com.thebund1st.tiantong.wechatpay.webhooks

import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult
import com.github.binarywang.wxpay.service.WxPayService
import com.thebund1st.tiantong.core.refund.OnlineRefund
import spock.lang.Specification

class WeChatPayNotifyPaymentResultCommandAssemblerTest extends Specification {

    private WeChatPayNotifyPaymentResultCommandAssembler subject
    private WxPayService wxPayService = Mock()

    def setup() {
        subject = new WeChatPayNotifyPaymentResultCommandAssembler(wxPayService)
    }

    def "it should parse raw notification to notify refund result command"() {
        given:
        def raw = """
            <xml>
                <return_code>SUCCESS</return_code>
                <appid><![CDATA[wx2421b1c4370ec43b]]></appid>
                <mch_id><![CDATA[10000100]]></mch_id>
                <nonce_str><![CDATA[TeqClE3i0mvn3DrK]]></nonce_str>
                <req_info><![CDATA[T87GAHG17TGAHG1TGHAHAHA1Y1CIOA9UGJH1GAHV871HAGAGQYQQPOOJMXNBCXBVNMNMAJAA]]></req_info>
            </xml>
        """
        def rawReqInfo = """
            <root>
                <out_refund_no><![CDATA[131811191610442717309]]></out_refund_no>
                <out_trade_no><![CDATA[71106718111915575302817]]></out_trade_no>
                <refund_account><![CDATA[REFUND_SOURCE_RECHARGE_FUNDS]]></refund_account>
                <refund_fee><![CDATA[3960]]></refund_fee>
                <refund_id><![CDATA[50000408942018111907145868882]]></refund_id>
                <refund_recv_accout><![CDATA[支付用户零钱]]></refund_recv_accout>
                <refund_request_source><![CDATA[API]]></refund_request_source>
                <refund_status><![CDATA[SUCCESS]]></refund_status>
                <settlement_refund_fee><![CDATA[3960]]></settlement_refund_fee>
                <settlement_total_fee><![CDATA[3960]]></settlement_total_fee>
                <success_time><![CDATA[2018-11-19 16:24:13]]></success_time>
                <total_fee><![CDATA[3960]]></total_fee>
                <transaction_id><![CDATA[4200000215201811190261405420]]></transaction_id>
            </root>
        """

        and:
        WxPayRefundNotifyResult.ReqInfo reqInfo = new WxPayRefundNotifyResult.ReqInfo()
        reqInfo.setOutRefundNo("a-unique-str")
        reqInfo.setRefundStatus("SUCCESS")
        WxPayRefundNotifyResult result = new WxPayRefundNotifyResult()
        result.setResultCode("SUCCESS")
        result.setReqInfo(reqInfo)
        result.setReqInfoString(rawReqInfo)
        wxPayService.parseRefundNotifyResult(raw) >> result


        when:

        def command = subject.toNotifyRefundResultCommand(raw)

        then:
        assert command.getOnlineRefundId() == OnlineRefund.Identifier.of(reqInfo.getOutRefundNo())
        assert command.getText() == rawReqInfo
    }
}
