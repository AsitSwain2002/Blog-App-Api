package com.org.Blog_App_Api.service;

import com.org.Blog_App_Api.dto.UpdatePasswordDataDto;

import jakarta.servlet.http.HttpServletRequest;

public interface HomeService {

	boolean verify(int userId, String vCode);

	boolean forgetPassword(String userName , HttpServletRequest req) throws Exception;

	boolean updatePassword(UpdatePasswordDataDto data);

	boolean verifyLink(int uId, String vId);
}
