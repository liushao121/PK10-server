package com.pk10.control;

import com.alibaba.fastjson.JSONObject;
import com.pk10.util.Const;
import com.pk10.util.Generator;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;


/**
 * Created by dengfengdecao on 16/9/19.
 * @see <a href=https://api.alidayu.com/doc2/apiDetail.htm?apiId=25450>
 *     https://api.alidayu.com/doc2/apiDetail.htm?apiId=25450</a>
 */
@SessionAttributes("captcha")
@RestController
@RequestMapping("sms")
public class SmsCaptchaController {

    public static final Logger log = LoggerFactory.getLogger(SmsCaptchaController.class);

    // 短信模板变量，传参规则{"key":"value"}
    @NotBlank
    private String smsParam;

    private String extend;


    @RequestMapping(value = "captcha/{recPhoneNum}", method = RequestMethod.POST)
    public Object getSmsCaptcha(ModelMap model, @PathVariable("recPhoneNum")String recPhoneNum) {
        String responseBody = null;
        if (recPhoneNum == null || recPhoneNum.trim().length() < 0 || recPhoneNum.trim().length() > 11) {
            model.addAttribute("error_msg", "请输入正确手机号!");
            return model.toString();
        }

        String captcha  = Generator.generateCaptcha();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("captcha", captcha);
        jsonObject.put("product", "数字互娱");

        TaobaoClient client = new DefaultTaobaoClient(Const.SERVER_URL, Const.APP_KEY, Const.APP_SECRET);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        // req.setExtend("123456");
        req.setSmsType(Const.SMS_TYPE);
        req.setSmsFreeSignName(Const.SMS_FREE_SIGN_NAME);
        req.setSmsTemplateCode(Const.SMS_TEMPLATE_CODE);
        req.setRecNum(recPhoneNum.trim());
        smsParam = jsonObject.toJSONString();
        log.debug("getSmsCaptcha: smsParam=" + smsParam);
        req.setSmsParamString(smsParam);

        try {
            AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
            model.addAttribute("captcha", captcha);

            responseBody = rsp.getBody();
            log.debug("getSmsCaptcha: phone ==> " + recPhoneNum.trim() + " responseBody <== " + responseBody);

            if (rsp.getResult() != null) {
                model.addAttribute("success_response", rsp.getResult());
            } else {
                model.addAttribute("error_response", rsp.getSubMsg());
            }
        } catch (ApiException e) {
            log.error("getSmsCaptcha : e <== " + e.getErrMsg());
        }

        JSONObject jsonBody = JSONObject.parseObject(responseBody);
        JSONObject successJson = jsonBody.getJSONObject("alibaba_aliqin_fc_sms_num_send_response");
        if (successJson != null) {
            successJson = successJson.getJSONObject("result");
            return successJson.getBoolean("success");
        } else {
            return false;
        }

    }


}
