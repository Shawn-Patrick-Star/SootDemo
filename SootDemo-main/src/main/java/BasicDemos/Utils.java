package BasicDemos;

import javafx.scene.shape.Path;
import soot.Printer;
import soot.Scene;
import soot.SootClass;
import soot.baf.BafASMBackend;
import soot.options.Options;
import soot.util.Chain;

import java.io.*;
import java.nio.file.Paths;
import java.util.LinkedList;

public class Utils {

    public static LinkedList<String> excludePathList;

    public static void initSootEnv(){

        String claspath = System.getProperty("java.class.path");
        Options.v().set_soot_classpath(claspath);

        Options.v().set_ignore_resolving_levels(true);
        Options.v().set_whole_program(true);
        Options.v().set_app(true);
        excludeJDKLibrary();
    }

    public static SootClass loadClass(String classname){

        try {
            Chain<SootClass> classes = Scene.v().getClasses();
            SootClass sootClass = Scene.v().forceResolve(classname, SootClass.BODIES);
            Scene.v().loadNecessaryClasses();
            return sootClass;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static void saveClassToPath(SootClass sootClass, String dirPath){

        String jimpleFilePath = Paths.get(dirPath, sootClass.getName() + ".jimple").toString();
        String classFilePath = Paths.get(dirPath, sootClass.getName() + ".class").toString();
        try{
            File dir = new File(dirPath);
            if (!dir.exists()) dir.mkdir();
            OutputStream jimpleStreamOut = new FileOutputStream(jimpleFilePath);
            PrintWriter jimpleWriterOut = new PrintWriter(new OutputStreamWriter(jimpleStreamOut));
            //01 jimple
            Printer.v().printTo(sootClass, jimpleWriterOut);
            jimpleStreamOut.flush();
            jimpleWriterOut.flush();
            jimpleStreamOut.close();
            jimpleWriterOut.close();

            OutputStream classStreamOut = new FileOutputStream(classFilePath);
            //class
            BafASMBackend backend = new BafASMBackend(sootClass, Options.v().java_version());
            backend.generateClassFile(classStreamOut);
            classStreamOut.flush();
            classStreamOut.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void excludeJDKLibrary() {
        Options.v().set_exclude(excludeList());
        Options.v().set_allow_phantom_refs(true);
    }

    private static LinkedList<String> excludeList() {

        if(excludePathList == null) {
            excludePathList = new LinkedList<String> ();
            excludePathList.add("java.");
            excludePathList.add("javax.");
            excludePathList.add("sun.");
            excludePathList.add("sunw.");
            excludePathList.add("com.sun.");
            excludePathList.add("com.ibm.");
            excludePathList.add("com.apple.");
            excludePathList.add("apple.awt.");
        }
        return excludePathList;
    }

}
