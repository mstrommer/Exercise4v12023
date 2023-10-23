package at.ac.fhcampuswien;

import at.ac.fhcampuswien.shape.Rectangle;
import org.junit.jupiter.api.*;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@Timeout(2)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AppTest {

    private PrintStream originalOut;
    private InputStream originalIn;
    private ByteArrayOutputStream bos;
    private PrintStream ps;

    @BeforeAll
    public static void init() {
        System.out.println("Testing Exercise2");
    }

    @AfterAll
    public static void finish() {
        System.out.println("Finished Testing Exercise2");
    }

    @BeforeEach
    public void setupStreams() throws IOException {
        originalOut = System.out;
        originalIn = System.in;

        bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos));

        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream(pos);
        System.setIn(pis);
        ps = new PrintStream(pos, true);
    }

    @AfterEach
    public void tearDownStreams() {
        // undo the binding in System
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    public void classReflection() {
        try {
            Class<?> c = Class.forName("at.ac.fhcampuswien.shape.Rectangle");
            // check if there are already fields declared
            assertTrue(c.getDeclaredFields().length != 0,"Class Rectangle hasn't declared any members yet.");
            // check if all fields are private
            assertTrue(Arrays.stream(c.getDeclaredFields()).allMatch(
                    field -> (Modifier.toString(field.getModifiers()).equals("private") && !field.getName().equals("count"))
                            || (field.getName().equals("count") && Modifier.toString(field.getModifiers()).equals("private static"))
            ), "Please check your field modifiers! (private, public, static,...)");
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void classMembersCountReflection() {
        try {
            Class<?> c = Class.forName("at.ac.fhcampuswien.shape.Rectangle");
            assertEquals(5, Arrays.stream(c.getDeclaredFields()).count(), "Member count of class Rectangle not correct.");
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void classConstructorsReflection() {
        try {
            Class<?> c = Class.forName("at.ac.fhcampuswien.shape.Rectangle");

            assertEquals(1,
                    Arrays.stream(c.getConstructors()).filter(constructor
                            -> constructor.toString().equals("public at.ac.fhcampuswien.shape.Rectangle(int,int,int,int)")).count(),
                    "Constructor (int,int,int,int) missing");
            assertEquals(1,
                    Arrays.stream(c.getConstructors()).filter(constructor
                            -> constructor.toString().equals("public at.ac.fhcampuswien.shape.Rectangle(int,int)")).count(),
                    "Constructor (int,int) missing");
            assertEquals(1,
                    Arrays.stream(c.getConstructors()).filter(constructor
                            -> constructor.toString().equals("public at.ac.fhcampuswien.shape.Rectangle(at.ac.fhcampuswien.shape.Rectangle)")).count(),
                    "Constructor (Rectangle) missing");

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @Order(1)
    public void getCount() {
        try {
            Class<?> c = Class.forName("at.ac.fhcampuswien.shape.Rectangle");
            Method m = c.getMethod("getCount");

            Constructor<?> co1 = c.getConstructor(int.class,int.class,int.class,int.class);
            Constructor<?> co2 = c.getConstructor(int.class,int.class);
            Constructor<?> co3 = c.getConstructor(Rectangle.class);

            Rectangle r1 = (Rectangle) co1.newInstance(2,2,4,4);
            Rectangle r2 = (Rectangle) co2.newInstance(2,2);
            Rectangle r3 = (Rectangle) co3.newInstance(r1);

            assertEquals(3, (int)m.invoke(null), "Rectangle count not correct.");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            fail("There should be a class called Rectangle.");
        } catch (NoSuchMethodException nsme){
            nsme.printStackTrace();
            fail("There should be a method called getCount().");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Problems might have occurred creating the Object. Also check return types.");
        }
    }

    @Test
    public void getWidth() {
        try {
            Class<?> c = Class.forName("at.ac.fhcampuswien.shape.Rectangle");
            Method m = c.getMethod("getWidth");
            Constructor<?> co = c.getConstructor(int.class,int.class,int.class,int.class);
            Rectangle r = (Rectangle) co.newInstance(2,2,7,4);
            assertEquals(5, (int) m.invoke(r), "Width not correctly computed.");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            fail("There should be a class called Rectangle.");
        } catch (NoSuchMethodException nsme){
            nsme.printStackTrace();
            fail("There should be a method called getWidth().");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Problems might have occurred creating the Object. Also check return types.");
        }
    }

    @Test
    public void getHeight() {
        try {
            Class<?> c = Class.forName("at.ac.fhcampuswien.shape.Rectangle");
            Method m = c.getMethod("getHeight");
            Constructor<?> co = c.getConstructor(int.class,int.class,int.class,int.class);
            Rectangle r = (Rectangle) co.newInstance(2,2,4,14);
            assertEquals(12, (int) m.invoke(r), "Height not correctly computed.");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            fail("There should be a class called Rectangle.");
        } catch (NoSuchMethodException nsme){
            nsme.printStackTrace();
            fail("There should be a method called getHeight().");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Problems might have occurred creating the Object. Also check return types.");
        }
    }

    @Test
    public void isInside1() {
        try {
            Class<?> c = Class.forName("at.ac.fhcampuswien.shape.Rectangle");
            Method m = c.getMethod("isInside", int.class, int.class);
            Constructor<?> co = c.getConstructor(int.class,int.class,int.class,int.class);
            Rectangle r = (Rectangle) co.newInstance(2,2,4,4);
            assertFalse((boolean)m.invoke(r,1,1), "Coordinates should be outside of the given rectangle");
            assertTrue((boolean)m.invoke(r,2,2), "Coordinates should be inside of the given rectangle");
            assertTrue((boolean)m.invoke(r,3,3), "Coordinates should be inside of the given rectangle");

        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            fail("There should be a class called Rectangle.");
        } catch (NoSuchMethodException nsme){
            nsme.printStackTrace();
            fail("There should be a method called isInside(int,int).");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Problems might have occurred creating the Object. Also check return types.");
        }
    }

    @Test
    public void isInside2() {
        try {
            Class<?> c = Class.forName("at.ac.fhcampuswien.shape.Rectangle");
            Method m = c.getMethod("isInside", Rectangle.class);
            Constructor<?> co = c.getConstructor(int.class,int.class,int.class,int.class);
            Rectangle r1 = (Rectangle) co.newInstance(1,1,3,3);
            Rectangle r2 = (Rectangle) co.newInstance(2,2,3,3);
            Rectangle r3 = (Rectangle) co.newInstance(5,5,6,7);
            assertTrue((boolean)m.invoke(r1,r2), "Rectangle should be within the other.");
            assertFalse((boolean)m.invoke(r1,r3), "Rectangle should be outside the other.");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            fail("There should be a class called Rectangle.");
        } catch (NoSuchMethodException nsme){
            nsme.printStackTrace();
            fail("There should be a method called isInside(Rectangle).");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Problems might have occurred creating the Object. Also check return types.");
        }
    }

    @Test
    public void rectangleToString() {
        try {
            Class<?> c = Class.forName("at.ac.fhcampuswien.shape.Rectangle");
            Constructor<?> co = c.getConstructor(int.class,int.class,int.class,int.class);
            Rectangle r = (Rectangle) co.newInstance(2,2,4,4);
            assertEquals(":Rectangle P1(2,2) P2(4,4)", r.toString(), "toString Format not correct.");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            fail("There should be a class called Rectangle.");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Problems might have occurred creating the Object. Also check return types.");
        }
    }

    @Test
    public void union1() {
        try {
            Class<?> c = Class.forName("at.ac.fhcampuswien.shape.Rectangle");
            Method m = c.getMethod("union", Rectangle.class);
            Constructor<?> co = c.getConstructor(int.class,int.class,int.class,int.class);
            Rectangle r1 = (Rectangle) co.newInstance(3,3,7,4);
            Rectangle r2 = (Rectangle) co.newInstance(1,1,3,3);
            Rectangle r3 = (Rectangle) m.invoke(r1,r2);
            assertEquals(":Rectangle P1(1,1) P2(7,4)", r3.toString(),"Union not computed correctly.");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            fail("There should be a class called Rectangle.");
        } catch (NoSuchMethodException nsme){
            nsme.printStackTrace();
            fail("There should be a method called union(Rectangle).");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Problems might have occurred creating the Object. Also check return types.");
        }
    }

    @Test
    public void union2() {
        try {
            Class<?> c = Class.forName("at.ac.fhcampuswien.shape.Rectangle");
            Method m = c.getMethod("union", Rectangle.class);
            Constructor<?> co = c.getConstructor(int.class,int.class,int.class,int.class);
            Rectangle r1 = (Rectangle) co.newInstance(4,4,6,6);
            Rectangle r2 = (Rectangle) co.newInstance(1,1,3,3);
            Rectangle r3 = (Rectangle) m.invoke(r1,r2);
            assertEquals(":Rectangle P1(1,1) P2(6,6)", r3.toString(),"Union not computed correctly.");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            fail("There should be a class called Rectangle.");
        } catch (NoSuchMethodException nsme){
            nsme.printStackTrace();
            fail("There should be a method called union(Rectangle).");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Problems might have occurred creating the Object. Also check return types.");
        }
    }

    @Test
    public void intersection1() {
        try {
            Class<?> c = Class.forName("at.ac.fhcampuswien.shape.Rectangle");
            Method m = c.getMethod("intersection", Rectangle.class);
            Constructor<?> co = c.getConstructor(int.class,int.class,int.class,int.class);
            Rectangle r1 = (Rectangle) co.newInstance(1,1,3,3);
            Rectangle r2 = (Rectangle) co.newInstance(2,2,3,3);
            Rectangle r3 = (Rectangle) m.invoke(r1,r2);
            assertEquals(":Rectangle P1(2,2) P2(3,3)", r3.toString(),"Intersection not computed correctly.");
            r1 = (Rectangle) co.newInstance(1,1,3,3);
            r2 = (Rectangle) co.newInstance(0,0,2,2);
            r3 = (Rectangle) m.invoke(r1,r2);
            assertEquals(":Rectangle P1(1,1) P2(2,2)", r3.toString(),"Intersection not computed correctly.");
            r1 = (Rectangle) co.newInstance(1,1,4,4);
            r2 = (Rectangle) co.newInstance(0,3,2,5);
            r3 = (Rectangle) m.invoke(r1,r2);
            assertEquals(":Rectangle P1(1,3) P2(2,4)", r3.toString(),"Intersection not computed correctly.");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            fail("There should be a class called Rectangle.");
        } catch (NoSuchMethodException nsme){
            nsme.printStackTrace();
            fail("There should be a method called intersection(Rectangle).");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Problems might have occurred creating the Object. Also check return types.");
        }
    }

    @Test
    public void intersection2() {
        try {
            Class<?> c = Class.forName("at.ac.fhcampuswien.shape.Rectangle");
            Method m = c.getMethod("intersection", Rectangle.class);
            Constructor<?> co = c.getConstructor(int.class,int.class,int.class,int.class);
            Rectangle r1 = (Rectangle) co.newInstance(1,1,3,3);
            Rectangle r2 = (Rectangle) co.newInstance(4,3,5,4);
            Rectangle r3 = (Rectangle) m.invoke(r1,r2);
            assertEquals(null, r3,"Intersection not computed correctly.");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            fail("There should be a class called Rectangle.");
        } catch (NoSuchMethodException nsme){
            nsme.printStackTrace();
            fail("There should be a method called intersection(Rectangle).");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Problems might have occurred creating the Object. Also check return types.");
        }
    }

    @Test
    public void generateRectangles() {
        try {
            Class<?> c = Class.forName("at.ac.fhcampuswien.App");
            Method m = c.getMethod("generateRectangles", int.class);
            Rectangle [] rects = (Rectangle[]) m.invoke(null,10);
            assertEquals(10,  Arrays.stream(rects).count(),"Number of Rectangles not correct.");
            assertTrue(Arrays.stream(rects).allMatch(Objects::nonNull),"Null values detected.");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            fail("There should be a class called Rectangle.");
        } catch (NoSuchMethodException nsme){
            nsme.printStackTrace();
            fail("There should be a method called generateRectangles(int).");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Problems might have occurred creating the Object. Also check return types.");
        }
    }

}