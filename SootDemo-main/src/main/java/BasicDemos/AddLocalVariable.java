package BasicDemos;

import soot.*;
import soot.jimple.AssignStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;

import java.util.ArrayList;
import java.util.List;

public class AddLocalVariable {

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
                //01 declare new local variable int newLocalVar;
                Local newVar = Jimple.v().newLocal("newLocalVar", IntType.v());
                sootMethodBody.getLocals().add(newVar);
                //02 assign new value to new variable newLocalVar = 999;
                AssignStmt assign = Jimple.v().newAssignStmt(newVar, IntConstant.v(9999));
                //03 insert the new statement before the first valid statement
                units.insertBefore(assign, validUnits.get(0));
            }
        }
        Utils.saveClassToPath(seedClass, dirpath);
    }
}
