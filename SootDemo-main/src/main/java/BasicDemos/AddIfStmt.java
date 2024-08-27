package BasicDemos;

import soot.*;
import soot.jimple.EqExpr;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;

import java.util.ArrayList;
import java.util.List;

public class AddIfStmt {

    public static final String dirpath = "sootOutput";

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
                //01 create a condition
                EqExpr cond = Jimple.v().newEqExpr(IntConstant.v(1), IntConstant.v(2));
                //02 set the target of the if statement
                IfStmt ifGoto = Jimple.v().newIfStmt(cond, validUnits.get(validUnits.size() - 1));
                //03 insert the new statement before the first valid statement
                units.insertBefore(ifGoto, validUnits.get(0));
            }
        }
        Utils.saveClassToPath(seedClass, dirpath);
    }
}
