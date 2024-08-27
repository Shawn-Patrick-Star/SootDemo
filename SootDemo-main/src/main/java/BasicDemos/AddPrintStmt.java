package BasicDemos;

import soot.*;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.StringConstant;

import java.util.ArrayList;
import java.util.List;

public class AddPrintStmt {

    public static String dirpath = "sootOutput";

    public static void main(String[] args) {

        String className = "BasicDemos.examples.HelloSoot";
        Utils.initSootEnv();
        SootClass seedClass = Utils.loadClass(className);
        List<SootMethod> sootMethods = seedClass.getMethods();

        for (SootMethod sootMethod : sootMethods) {

            Body sootMethodBody = sootMethod.retrieveActiveBody();
            if (sootMethod.getName().equals("main")){

                UnitPatchingChain units = sootMethodBody.getUnits();
                List<Unit> validUnits = new ArrayList<>();
                for (Unit unit : sootMethodBody.getUnits()) {
                    if (! unit.toString().contains(":= @")){
                        validUnits.add(unit);
                    }
                }

                //01 add java.io.PrintStream out;
                Local out = Jimple.v().newLocal("out", RefType.v("java.io.PrintStream"));
                sootMethodBody.getLocals().add(out);
                //02 out = <java.lang.System: java.io.PrintStream out>;
                AssignStmt assign = Jimple.v().newAssignStmt(out,
                        Jimple.v().newStaticFieldRef(Scene.v().getField("<java.lang.System: java.io.PrintStream out>").makeRef()));
                //03 virtualinvoke $r0.<java.io.PrintStream: void println(java.lang.String)>("opt function...");
                SootMethod call = Scene.v().getMethod("<java.io.PrintStream: void println(java.lang.String)>");
                InvokeStmt invokeCall = Jimple.v().newInvokeStmt(Jimple.v().newVirtualInvokeExpr(out, call.makeRef(), StringConstant.v("Hello Soot from AddPrintStmt!")));
                //04 insert the new statement before the first valid statement
                units.insertBefore(assign, validUnits.get(0));
                units.insertBefore(invokeCall, validUnits.get(0));
            }
        }
        Utils.saveClassToPath(seedClass, dirpath);
    }
}
