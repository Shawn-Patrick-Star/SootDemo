package BasicDemos.Work;

import BasicDemos.Utility.Utility;
import BasicDemos.Utils;
import soot.*;
import soot.jimple.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddMethod {
    public static String dirpath = "sootOutput";

    public static void main(String[] args) {

        String className = "BasicDemos.examples.HelloSoot";
        Utils.initSootEnv();
        SootClass seedClass = Utils.loadClass(className);

        SootMethod newMethod = new SootMethod("newMethodName",
                Collections.emptyList(),
                VoidType.v(),
                Modifier.STATIC | Modifier.PUBLIC);
        seedClass.addMethod(newMethod);
        Body newMethodBody = Jimple.v().newBody(newMethod); // 创建一个新的方法体
        newMethod.setActiveBody(newMethodBody); // 设置活跃的方法体
// 方法体代码
        List<Unit> printUnits = Utility.GetPrintUnits(newMethodBody, "Hello, Soot!");
        newMethodBody.getUnits().addAll(printUnits);
// ---------
        ReturnVoidStmt returnStmt = Jimple.v().newReturnVoidStmt();
        newMethodBody.getUnits().add(returnStmt);

        Utils.saveClassToPath(seedClass, dirpath);
    }
}
