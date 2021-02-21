package aes.rest;

import aes.service.AESService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@Api(protocols = "http, https")
@RequestMapping(value = "v1/aes")
public class AESController {

    private final AESService aesService;

    @Autowired
    public AESController(AESService aesService) {
        this.aesService = aesService;
    }

    @RequestMapping(value = "/encrypt", method = RequestMethod.POST)
    @ApiOperation(value = "Encrypt text")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 500, message = "SomeError")
    })
    public EncryptionResponse encryptText(@RequestBody EncryptionRequest request) {

        Triple<String, String, String> encryptionData = aesService.encryptText(request.getTextToEncrypt());
        return new EncryptionResponse(encryptionData.getLeft(), encryptionData.getMiddle(), encryptionData.getRight());
    }

    @RequestMapping(value = "/decrypt", method = RequestMethod.POST)
    @ApiOperation(value = "Decrypt text")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 500, message = "SomeError")
    })
    public DecryptionResponse decryptText(@RequestBody DecryptionRequest request) {
        Pair<String, String> decryptionResult = aesService.decryptText(request.getTextToDecrypt(), request.getKey());
        return new DecryptionResponse(decryptionResult.getKey(), decryptionResult.getRight());
    }
}
