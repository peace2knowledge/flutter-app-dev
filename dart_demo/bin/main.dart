import 'package:dart_demo/dart_demo.dart' as dart_demo;

void printInts(List<int> a) => print(a);

void main() {
  var list = <Object>[];
  list.add(1);
  list.add("2");
  printInts(list);
}
