import 'dart:io';

import 'package:path_provider/path_provider.dart';

class FileManager{

  Future<bool> _fileExists(String fileName) async {
    String filePath = await getFilePath(fileName);
    File file = File(filePath);
    return file.exists();
  }

  Future<String> getFilePath(String fileName) async {
    Directory? appDocDir = await getExternalStorageDirectory();
    String? appDocPath = appDocDir?.path;
    return '$appDocPath/$fileName';
  }

  Future<void> createFile(String fileName) async {
    if (await _fileExists(fileName)) {
      return;
    }

    String filePath = await getFilePath(fileName);
    File file = File(filePath);
    print('Creating file: $filePath');
    await file.create();
  }

  Future<void> writeToFile(String fileName, String content) async {
    String filePath = await getFilePath(fileName);
    File file = File(filePath);
    await file.writeAsString(content, mode: FileMode.append);
  }

}