
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class GestionRecetas {
    private static Scanner sc=new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("--Elige una opción--\n+" +
                        "1.Mostrar todas las recetas almacenadas\n" +
                "2.Buscar una receta mediante su identificador\n" +
                "3.Añadir nuevas recetas\n" +
                "4.Modificar el tiempo de preparación y el número de raciones de una\n" +
                "receta\n" +
                "5.Eliminar una receta del catálogo\n" +
                "6.Salir");
        boolean salir = false;
        while (!salir){
            int opcion= Integer.parseInt(sc.nextLine());
            switch (opcion){
                case 1:
                    mostrarRecetas();
                    break;
                case 2:
                    System.out.println("Introduce el id a buscar: ");
                    int id=Integer.parseInt(sc.nextLine());
                    buscarPorId(id);
                    break;
                case 3:
                    System.out.println("Introduce el nombre: ");
                    String nombre=sc.nextLine();
                    System.out.println("Introduce la categoria: ");
                    String categoria=sc.nextLine();
                    System.out.println("Introduce el tiempo de preparación");
                    int tiempoP=Integer.parseInt(sc.nextLine());
                    System.out.println("Introduce la dificultas: ");
                    String dificutal= sc.nextLine();
                    System.out.println("Introduce las raciones: ");
                    int raciones=Integer.parseInt(sc.nextLine());
                    añadirReceta(nombre,categoria,tiempoP,dificutal,raciones);
                    break;
                case 4:

                    break;
                case 5:

                    break;
                case 6:
                    System.out.println("Saliendo...");
                    salir = true;
                    break;
                default:
                    System.out.println("Opción Incorrecta");
            }
        }

    }

    public static void mostrarRecetas(){
        String sql="select * from recetas";

        try(Connection conexion=ConexionBD.getConexion(); PreparedStatement pstmt=conexion.prepareStatement(sql); ResultSet rs= pstmt.executeQuery()) {
           while(rs.next()){
               int id= rs.getInt("id");
               String nombre= rs.getString("nombre");
               String categoria= rs.getString("categoria");
               int tiempoPreparacion= rs.getInt("tiempo_preparacion");
               String dificultad= rs.getString("dificultad");
               int raciones= rs.getInt("raciones");
               Receta r = new Receta(id, nombre, categoria, tiempoPreparacion, dificultad, raciones);
               System.out.println(r);

           }
        } catch (SQLException e) {
            System.out.println("Error en la ejecucción sql ");
        }
    }

    public static void buscarPorId(int id){

    }

    public static void añadirReceta(String nombre,String categoria,int tiempoPreparacion,String dificultad,int raciones){

    }
    public static void modificar(){

    }
    public static void eliminar(int id){

    }
}
