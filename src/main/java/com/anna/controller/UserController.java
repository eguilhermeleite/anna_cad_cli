package com.anna.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anna.domain.User;
import com.anna.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/cadcli")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// chaves de desencriptar e encriptar
	String decKey = "NV2M5TnBxtHznZiBF85yNEP1FbnPPqvD";
	String encKey = "lgmsTAiDqINHDQgu58gM2d3AKpPwV/tM";

	@Autowired
	private UserRepository userRepository;

	public UserController() {
		super();
	}

	@PostMapping
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {

			// variaveis vindas no corpo da requisicao
			String ivReceived = req.getParameter("ANNAEXEC");// esse é o iv dinâmico vindo do AnnA
			String cpfRec = req.getParameter("cpf");// cep digitado pelo usuario
			String namRec = req.getParameter("name");// nome digitado pelo usuario
			String emaRec = req.getParameter("email");// email digitado pelo usuario

			// obter os bytes das chaves ANNAEXC, chave de desencriptar e chave de encriptar
			byte[] ivDecoded = Base64.getDecoder().decode(ivReceived);
			byte[] decKeyDecoded = Base64.getDecoder().decode(decKey);
			byte[] encKeyDecoded = Base64.getDecoder().decode(encKey);

			// associar as chaves aos tipos necessarios para os metodos de encrip./desencr.
			IvParameterSpec iv = new IvParameterSpec(ivDecoded);
			SecretKey dKey = new SecretKeySpec(decKeyDecoded, "DESede");
			SecretKey eKey = new SecretKeySpec(encKeyDecoded, "DESede");

			// agora e possivel desencriptar os valores digitados
			String cpf = decrypt(cpfRec, dKey, iv);
			String name = decrypt(cpfRec, dKey, iv);
			String email = decrypt(cpfRec, dKey, iv);
			
			User user = new User(cpf, name, email);
			userRepository.save(user);

		} catch (Exception e) {

		}

	}// doPost

	public String decrypt(String encMessage, SecretKey key, IvParameterSpec iv)
			throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		byte[] message = Base64.getDecoder().decode(encMessage.getBytes("utf-8"));
		final Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		decipher.init(Cipher.DECRYPT_MODE, key, iv);
		byte[] plainText = decipher.doFinal(message);
		return new String(plainText, "UTF-8");
	}

}
