package aes.rest;

import aes.jwt.authorization.UserToken;
import aes.jwt.authorization.Utils;
import aes.service.AESService;
import com.nimbusds.jose.JOSEException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
    public EncryptionResponse encryptText(@RequestBody EncryptionRequest request) {
        UserToken serviceUser = Utils.getServiceUser();
        if (serviceUser == null || !serviceUser.getUserRole().equals("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current user is not authorized to use this endpoint");
        }

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
        UserToken serviceUser = Utils.getServiceUser();
        if (serviceUser == null || !serviceUser.getUserRole().equals("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current user is not authorized to use this endpoint");
        }
        Pair<String, String> decryptionResult = aesService.decryptText(request.getTextToDecrypt(), request.getKey());
        return new DecryptionResponse(decryptionResult.getKey(), decryptionResult.getRight());
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "Login and generate access token")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userName", value = "user name", dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "password", value = "password", dataType = "string", paramType = "query")
    })
    public String authorize(String userName, String password) throws JOSEException {
        return aesService.generateToken(userName, userName, password);
    }
}
