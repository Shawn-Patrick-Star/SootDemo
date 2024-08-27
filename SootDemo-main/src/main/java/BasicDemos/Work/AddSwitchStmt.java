package BasicDemos.Work;

import BasicDemos.Utility.Utility;
import BasicDemos.Utils;
import soot.*;
import soot.jimple.*;

import java.util.ArrayList;
import java.util.List;

public class AddSwitchStmt {
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

                List<IntConstant> keyList = new ArrayList<>();
                keyList.add(IntConstant.v(1));
                keyList.add(IntConstant.v(2));
                keyList.add(IntConstant.v(3));

                List<List<Unit>> caseContentList = new ArrayList<>();
                caseContentList.add(Utility.GetPrintUnits(sootMethodBody, "Case 1", "out1"));
                caseContentList.add(Utility.GetPrintUnits(sootMethodBody, "Case 2", "out2"));
                caseContentList.add(Utility.GetPrintUnits(sootMethodBody, "Case 3", "out3"));

                List<Unit> defaultContent = Utility.GetPrintUnits(sootMethodBody, "Default");

                List<Unit> unitsList = Utility.GetSwitchUnits(sootMethodBody, keyList, caseContentList, defaultContent);
                units.insertBefore(unitsList, validUnits.get(0));

            }
        }
        Utils.saveClassToPath(seedClass, dirpath);

    }

}
