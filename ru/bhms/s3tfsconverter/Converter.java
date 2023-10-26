package ru.bhms.s3tfsconverter;

import java.io.File;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;

import static java.lang.System.out;

public class Converter {
	public static final String FOLDER_DEFAULT = "C:/Program Files";
	public static final boolean MADE;
	public static DataInputStream tfs;
	public static ZipOutputStream ext;
	public static int fileCount;
	
	static {
		boolean made = false;
		if(initFolders()) made = true;
		MADE = made;
	}
	
	public static final boolean initFolders() {
		boolean r = false;
		File src;
		if(!(src = new File(FOLDER_DEFAULT + "/SamsungS3-fs")).exists()) {
			src.mkdir();
			r = true;
		}
		if(!(src = new File(FOLDER_DEFAULT + "/SamsungS3-fs/tfs")).exists()) src.mkdir();
		if(!(src = new File(FOLDER_DEFAULT + "/SamsungS3-fs/ext")).exists()) src.mkdir();
		return r;
	}
	
	public static void main(String[] args) throws IOException {
		boolean shouldRun = true;
		out.println("Samsung S3 model TFS/CSC file converter v1.0\nDeveloped and published by BHms game");
		if(MADE) out.println("No program files were found on your disk, new files were created. Path: " + FOLDER_DEFAULT + "/SamsungS3-fs");
		if(args.length < 1) {
			out.println("\nUsage: java -jar SamsungS3_tfs_converter.jar <TFS file name> [...]");
			return;
		}
		out.println("Initializing streams...");
		try {
			tfs = new DataInputStream(new FileInputStream(FOLDER_DEFAULT + "/SamsungS3-fs/tfs/" + args[0]));
			ext = new ZipOutputStream(new FileOutputStream(FOLDER_DEFAULT + "/SamsungS3-fs/ext/" + args[0] + ".zip"));
		} catch(Exception e) {
			if(tfs != null) tfs.close();
			if(ext != null) ext.close();
			e.printStackTrace();
			shouldRun = false;
		}
		if(!shouldRun) return;
		try {
			run();
			tfs.close();
			ext.finish();
			ext.close();
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
		out.println("Extracted and archived: " + fileCount + " files total");
	}
	
	private static final void run() throws IOException {
		tfs.skip(20);
		fileCount = endianTranslate(tfs.readInt());
		out.println("Number of files: " + fileCount);
		tfs.skip(8);
		out.println("Ready to extract");
		for(int cn = 0;cn < fileCount;cn++) {
			extract(endianTranslate(tfs.readInt()));
		}
	}
	
	private static final void extract(int size) throws IOException {
		byte[] filename = new byte[264];
		tfs.read(filename);
		String s;
		ext.putNextEntry(new ZipEntry(s = new String(filename).replace(String.valueOf((char)0),"")));
		out.println("Extracting file: " + s);
		byte[] file = new byte[size];
		tfs.read(file);
		ext.write(file);
		ext.flush();
		ext.closeEntry();
	}
	
	public static final int endianTranslate(int src) throws IOException {
		ByteArrayOutputStream bnote = new ByteArrayOutputStream();
		new DataOutputStream(bnote).writeInt(src);
		byte[] b = bnote.toByteArray();
		byte[] r = new byte[4];
		for(int i = 0;i < 4;i++) {
			r[i] = b[3 - i];
		}
		return new DataInputStream(new ByteArrayInputStream(r)).readInt();
	}
}
