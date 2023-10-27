# Samsung-S3-tfs-converter
The program is written in Java and is designed to convert Samsung S3 (not only S3) phone firmware files TFS/CSC (without CFG/CCF file configuration) to a zip files.

The program will not convert TFS/CSC firmware files with CFG/CCF configuration files to zip archive, 'cause this is a different file structure.

# A short guide to the program
Before you start using the program, first run it without command line arguments:
```
java -jar SamsungS3_tfs_converter.jar
```
This way you create program files and also get the path to the main directory with program files.

Only 2 directories that will be created in main program directory:
```
C:/Program Files/S3-tfs/ext (converted TFS/CSC files)
C:/Program Files/S3-tfs/tfs (all TFS/CSC files)
```

Move the firmware file to the "tfs" folder, and for example it will be a file named "CSC_C3010OXEII1.csc".

Afterwards, run the program with the following command line arguments:
```
java -jar SamsungS3_tfs_converter.jar CSC_C3010OXEII1.csc
```

If everything is written correctly, then the program will begin to operate, and read the data bytes from the firmware file, and write it to the zip archive, which is located in the "ext" directory, or else the program will throw IOException.
