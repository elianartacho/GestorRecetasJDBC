
import java.sql.*;
import java.util.Scanner;

public class GestionRecetas {
    private static Scanner sc=new Scanner(System.in);
    public static void main(String[] args) {

        boolean salir = false;
        int opcion=0;
        while (!salir){
            System.out.println("--Elige una opción--\n" +
                    "1.Mostrar todas las recetas almacenadas\n" +
                    "2.Buscar una receta mediante su identificador\n" +
                    "3.Añadir nuevas recetas\n" +
                    "4.Modificar el tiempo de preparación y el número de raciones de una\n" +
                    "receta\n" +
                    "5.Eliminar una receta del catálogo\n" +
                    "6.Salir");
            try {
              opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número válido, por favor.");
            }
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
                    System.out.println("Introduce la dificultad: ");
                    String dificutad= sc.nextLine();
                    System.out.println("Introduce las raciones: ");
                    int raciones=Integer.parseInt(sc.nextLine());
                    añadirReceta(nombre,categoria,tiempoP,dificutad,raciones);

                    break;
                case 4:
                    System.out.println("Introduce el id a buscar: ");
                    int id2=Integer.parseInt(sc.nextLine());
                    System.out.println("Introduce el nuevo tiempo de preparación");
                    int tiempoP2=Integer.parseInt(sc.nextLine());
                    System.out.println("Introduce las nuevas raciones: ");
                    int raciones2=Integer.parseInt(sc.nextLine());
                    modificar(id2,tiempoP2,raciones2);
                    mostrarRecetas();
                    break;
                case 5:
                    System.out.println("Introduce el id a eliminar: ");
                    int id3=Integer.parseInt(sc.nextLine());
                    eliminar(id3);
                    mostrarRecetas();
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

    public static void buscarPorId(int idRecibido){
        String sql = "{CALL buscarReceta(?)}";
        try(Connection con = ConexionBD.getConexion(); CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setInt(1, idRecibido);
            ResultSet rs=cstmt.executeQuery();
            boolean encontrado = false;
            while(rs.next()){
                encontrado=true;
                int  id=rs.getInt("id");
                String nombre= rs.getString("nombre");
                String categoria= rs.getString("categoria");
                int tiempoPreparacion= rs.getInt("tiempo_preparacion");
                String dificultad= rs.getString("dificultad");
                int raciones= rs.getInt("raciones");
                Receta r = new Receta(idRecibido, nombre, categoria, tiempoPreparacion, dificultad, raciones);
                System.out.println(r);

            }
            if (!encontrado) {
                System.out.println("No se ha encontrado ninguna receta con el ID " + idRecibido);
            }
        } catch (SQLException e) {
            System.out.println("Error en la ejecucción sql ");

        }
    }

    public static void añadirReceta(String nombre,String categoria,int tiempoPreparacion,String dificultad,int raciones){
        String sql="Insert into recetas(nombre,categoria,tiempo_preparacion,dificultad,raciones)values(?,?,?,?,?)";
        String sql2="update estadisticas set total_recetas = total_recetas + 1 where id=1";
        Connection conexion=null;
        PreparedStatement pstmt=null;
        PreparedStatement pstmt2=null;
        try {
            conexion=ConexionBD.getConexion();
            conexion.setAutoCommit(false);
            pstmt= conexion.prepareStatement(sql);
            pstmt2=conexion.prepareStatement(sql2);

            pstmt.setString(1,nombre);
            pstmt.setString(2,categoria);
            pstmt.setInt(3,tiempoPreparacion);
            pstmt.setString(4,dificultad);
            pstmt.setInt(5,raciones);
            pstmt.executeUpdate();
            pstmt2.executeUpdate();
            conexion.commit();
            System.out.println("Transaccíon confirmada");
            Receta receta= new Receta(nombre, categoria, tiempoPreparacion, dificultad, raciones);
            System.out.println(receta);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            try {
                if (conexion != null) {
                    conexion.rollback();
                    System.out.println("Transacción cancelada.");
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        } finally {
            try {

                if (pstmt != null) {
                    pstmt.close();
                }

                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }


    }

    public static void modificar(int id,int tiempoPreparacion,int raciones){
        String sql="update recetas set tiempo_preparacion=?,raciones=? where id=?";
        try(Connection conexion=ConexionBD.getConexion(); PreparedStatement pstmt=conexion.prepareStatement(sql)){
            pstmt.setInt(1,tiempoPreparacion);
            pstmt.setInt(2,raciones);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
            System.out.println("Actualizada correctamente");

        } catch (SQLException e) {
            System.out.println("Error en la ejecucción sql ");
        }

    }
    public static void eliminar(int id) {
        String sql = "delete from recetas where id= ?";
        String sql2 = "update estadisticas set total_recetas = total_recetas - 1 where id=1";

        Connection conexion = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;

        try {
            conexion = ConexionBD.getConexion();
            conexion.setAutoCommit(false);

            pstmt1 = conexion.prepareStatement(sql);
            pstmt1.setInt(1, id);
            pstmt1.executeUpdate();

            pstmt2 = conexion.prepareStatement(sql2);
            pstmt2.executeUpdate();

            conexion.commit();
            System.out.println("Eliminado correctamente ");

        } catch (SQLException e) {
            System.out.println("Error en la ejecución sql ");
            try {
                if (conexion != null) {
                    conexion.rollback();
                    System.out.println("Transacción cancelada.");
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        } finally {
            try {
                if (pstmt1 != null) pstmt1.close();
                if (pstmt2 != null) pstmt2.close();
                if (conexion != null) conexion.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
