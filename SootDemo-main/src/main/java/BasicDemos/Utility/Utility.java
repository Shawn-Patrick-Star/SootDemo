package BasicDemos.Utility;

import soot.*;
import soot.jimple.*;

import java.util.ArrayList;
import java.util.List;

public class Utility {

    public static List<Unit> GetPrintUnits(Body sootMethodBody, String context, String LocalVar) {
        Local out = Jimple.v().newLocal(LocalVar, RefType.v("java.io.PrintStream"));
        sootMethodBody.getLocals().add(out);

        AssignStmt assign = Jimple.v().newAssignStmt(
                out,
                Jimple.v().newStaticFieldRef(Scene.v().getField("<java.lang.System: java.io.PrintStream out>").makeRef())
        );

        InvokeStmt invokeCall = Jimple.v().newInvokeStmt(
                Jimple.v().newVirtualInvokeExpr(
                        out,
                        Scene.v().getMethod("<java.io.PrintStream: void println(java.lang.String)>").makeRef(),
                        StringConstant.v(context)
                )
        );

        List<Unit> units = new ArrayList<>();
        units.add(assign);
        units.add(invokeCall);

        return units;
    }
    public static List<Unit> GetPrintUnits(Body sootMethodBody, String context) {
        return GetPrintUnits(sootMethodBody, context, "out");
    }

    public static List<Unit> GetForUnits(Body sootMethodBody, List<Unit> LoopContent) {
        // 01 声明变量 i
        Local newVar_i = Jimple.v().newLocal("i", IntType.v());
        sootMethodBody.getLocals().add(newVar_i);

        // 02 i = 0
        AssignStmt assignStmt = Jimple.v().newAssignStmt(newVar_i, IntConstant.v(0));

        Unit end = Jimple.v().newNopStmt();

        // 03 i < 100 循环条件判断
        GeExpr cond_front = Jimple.v().newGeExpr(newVar_i, IntConstant.v(100));
        IfStmt ifnotGoto_end = Jimple.v().newIfStmt(cond_front, end);

        // 04 i++
        AddExpr addExpr = Jimple.v().newAddExpr(newVar_i, IntConstant.v(1));
        AssignStmt add_i = Jimple.v().newAssignStmt(newVar_i, addExpr);

        // 05 返回到循环条件判断
        GotoStmt Goto_start = Jimple.v().newGotoStmt(ifnotGoto_end);

        List<Unit> units = new ArrayList<>();
        units.add(assignStmt);
        units.add(ifnotGoto_end);
        units.addAll(LoopContent);
        units.add(add_i);
        units.add(Goto_start);
        units.add(end);

        return units;
    }

    public static List<Unit> GetTryUnits(Body sootMethodBody, List<Unit> tryContent, List<Unit> catchContent) {

        Local exceptionLocal = Jimple.v().newLocal("r2", RefType.v("java.lang.RuntimeException"));
        sootMethodBody.getLocals().add(exceptionLocal);
        IdentityStmt identityStmt = Jimple.v().newIdentityStmt(exceptionLocal, Jimple.v().newCaughtExceptionRef());
        catchContent.add(0, identityStmt);

        catchContent.add(Jimple.v().newNopStmt());
        tryContent.add(Jimple.v().newGotoStmt(catchContent.get(catchContent.size() - 1)));

        SootClass exceptionClass = Scene.v().getSootClass("java.lang.RuntimeException");
        Trap trap = Jimple.v().newTrap(
                exceptionClass,
                tryContent.get(0),
                tryContent.get(tryContent.size() - 1),
                catchContent.get(0)
        );

        List<Unit> units = new ArrayList<>(tryContent);
        units.addAll(catchContent);
        sootMethodBody.getTraps().add(trap);

        return units;

    }

    public static List<Unit> GetSwitchUnits(Body sootMethodBody, List<IntConstant> keyList, List<List<Unit>> caseContentList, List<Unit> defaultContent) {
        // 01 声明变量 i
        Local switchVar = Jimple.v().newLocal("i", IntType.v());
        sootMethodBody.getLocals().add(switchVar);

        // 02 i = 0
        AssignStmt assignStmt = Jimple.v().newAssignStmt(switchVar, IntConstant.v(0));

        // 03 switch
        List<Unit> caseList = new ArrayList<>();
        for(int i = 0; i < keyList.size(); i++) {
            caseList.add(caseContentList.get(i).get(0));
        }

        LookupSwitchStmt lookupSwitchStmt = Jimple.v().newLookupSwitchStmt(switchVar, keyList, caseList, defaultContent.get(0));

        List<Unit> units = new ArrayList<>();
        units.add(assignStmt);
        units.add(lookupSwitchStmt);

        for (int i = 0; i < keyList.size(); i++) {
            units.addAll(caseContentList.get(i));
        }
        units.addAll(defaultContent);

        return units;
    }

    public static List<Unit> GetDo_WhileUnits(Body sootMethodBody, List<Unit> LoopContent) {
        // 01 声明变量 i
        Local newVar_i = Jimple.v().newLocal("i", IntType.v());
        sootMethodBody.getLocals().add(newVar_i);

        // 02 i = 0
        AssignStmt assignStmt = Jimple.v().newAssignStmt(newVar_i, IntConstant.v(0));

        // 03 i++
        AssignStmt add = Jimple.v().newAssignStmt(newVar_i, Jimple.v().newAddExpr(newVar_i, IntConstant.v(1)));

        // 04 do { i++ } while (i < 100)
        LtExpr cond = Jimple.v().newLtExpr(newVar_i, IntConstant.v(100));
        IfStmt ifGoto = Jimple.v().newIfStmt(cond, LoopContent.get(0));

        List<Unit> units = new ArrayList<>();
        units.add(assignStmt);
        units.addAll(LoopContent);
        units.add(add);
        units.add(ifGoto);

        return units;
    }

    public static void AddMethod(SootClass seedClass, String methodName, List<Type> paramTypes, Type returnType, int modifier, List<Unit> methodBody) {
        SootMethod newMethod = new SootMethod(
                methodName,
                paramTypes,
                returnType,
                Modifier.STATIC | Modifier.PUBLIC);
        seedClass.addMethod(newMethod);

        Body newMethodBody = Jimple.v().newBody(newMethod); // 创建一个新的方法体
        newMethod.setActiveBody(newMethodBody); // 设置活跃的方法体

        newMethodBody.getUnits().addAll(methodBody);
    }

}
