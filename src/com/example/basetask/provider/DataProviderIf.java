package com.example.basetask.provider;

import java.util.List;

import android.graphics.pdf.PdfDocument.PageInfo;

import com.example.basetask.model.GitHubUser;

public interface DataProviderIf {

	List<GitHubUser> getUsers(PageInfo aPageInfo);

}
