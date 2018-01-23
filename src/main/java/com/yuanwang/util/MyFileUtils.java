package com.yuanwang.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

public class MyFileUtils {

	private static Logger logger  =  Logger.getLogger(MyFileUtils. class );

	public static String getRootPath(){
		return MyFileUtils.class.getClassLoader().getResource("").getPath();
	}
	/**
	 * 上传文件
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static File uploadFile(MultipartFile file) throws Exception {

		if (!file.isEmpty()) {
			InputStream in = null;
			OutputStream out = null;
			File serverFile = null;
			try {
				String rootPath =getRootPath();
				File dir = new File(rootPath + File.separator + "tmpFiles");
				if (!dir.exists()) {
					dir.mkdirs();
				}
				serverFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());
				in = file.getInputStream();
				out = new FileOutputStream(serverFile);
				byte[] b = new byte[1024];
				int len = 0;
				while ((len = in.read(b)) > 0) {
					out.write(b, 0, len);
				}
				out.close();
				in.close();
				logger.info("Server File Location=" + serverFile.getAbsolutePath());
			} catch (Exception e) {
				logger.error(e);
			} finally {
				if (out != null) {
					out.close();
					out = null;
				}

				if (in != null) {
					in.close();
					in = null;
				}
			}
			return serverFile;
		} else {
			return null;
		}
	}

	/**
	 * 判断 文件是否存在
	 * @param filePath
	 * @return
	 */
	public static Boolean existsFile(String filePath){
		if(StringUtils.isBlank(filePath)){
			return false;
		}
		File file=new File(filePath);
		if(file.exists()){
			return true;
		}else{
			return false;
		}
	}

}
