import 'package:flutter/material.dart';

void main() => runApp(new MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: 'Flutter Container Demo',
      theme: new ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: Center(
        child: Container(
          color: Colors.green,
          child: Text("Flutter CheatSheet."),
          alignment: Alignment(0.0, 1.5),
        ),
      ),
    );
  }
}
