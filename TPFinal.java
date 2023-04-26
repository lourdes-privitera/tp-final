package conexion.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 Procedimiento para conectar la DB y realizar una consulta de las tablas.
 */
public class TPFinal {
    Connection conexion = null;
    Statement sentencia = null; // 3. Crear la sentencia - Este método es usado para crear la sentencia.
    							// Esta sentencia es la responsable de ejecutar las consultas a la DB.

   
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        TPFinal m = new TPFinal();

        m.conectar();    //CONECTO LA BBDD ANTES DE INICIAR EL MENÚ
        boolean salir = false;
        do {
            switch (menuPrin()) {
            	case 1:
            		m.consultarTabla();  //Cuando pulse la opción 1 del menú me llevará a la función CONSULTAR
            		break;
            	case 2:
            		m.agregarTablaPac();    //Cuando pulse la opción 2 del menú me llevará a la función AGREGAR PACIENTE
            		break;
            	case 3:
            		m.eliminarTablaPac(); //Cuando pulse la opción 3 del menú me llevará a la función ELIMINAR PACIENTE
            		break;	
            	case 4:
            		m.agregarTablaMed();    //Cuando pulse la opción 4 del menú me llevará a la función AGREGAR MEDICO
            		break;
            	case 5:
            		m.eliminarTablaMed(); //Cuando pulse la opción 5 del menú me llevará a la función ELIMINAR MEDICO
            		break;	
                case 0:
                    System.out.println("Vuelva pronto");
                    m.desconectar();               //CUANDO PULSE EL 0 CIERRO LA BBDD Y CIERRO LA APL.
                    salir = true;
                    break;
                default:
                    System.out.println("Opción incorrecta");
                    break;
            }
        } while (!salir);

    } //fin main
//-------------------------------------------------------------------------------
    
    //MENU PRINCIPAL:
    private static int menuPrin() {

        Scanner sc = new Scanner(System.in);

        System.out.println("--------------------------------");
        System.out.println("Conexión de bbdd MySQL");
        System.out.println("--------------------------------");
        System.out.println("5.ELIMINAR UN REGISTRO EN LA TABLA MEDICOS");
        System.out.println("4.INSERTAR UN REGISTRO EN LA TABLA MEDICOS");
        System.out.println("3.ELIMINAR UN REGISTRO EN LA TABLA PACIENTES");
        System.out.println("2.INSERTAR UN REGISTRO EN LA TABLA PACIENTES");
        System.out.println("1.MOSTRAR EL CONTENIDO DE LAS TABLAS");
        System.out.println("0.SALIR");
        System.out.println("\n Por favor, escoja una opción.");
        System.out.println("--------------------------------");

        return sc.nextInt(); //Recibo un entero

    }//Fin menuPrincipal

//-----------------------------------------------------------------------------------------------
    
/*MÉTODO QUE CONECTA CON LA BBDD DE MYSQL*/
    public void conectar() {
        try {
            Class.forName("com.mysql.jdbc.Driver"); //1. Registrar la ‘clase’ del driver*/
             conexion = DriverManager.getConnection("jdbc:mysql://localhost/consultorio", "root", "Greiscol01");
            //2. Crear el objeto de conexión - Este método se utiliza para establecer conexión con la DB.
            // aca ingresamos el camino y el nombre de la DB
            System.out.println("**************************************");
            System.out.println(" * CONEXIÓN REALIZADA CORRECTAMENTE * ");
            System.out.println("**************************************");
        } catch (Exception e) {
            System.out.println("*****************************************");
            System.out.println(" * NO SE HA PODIDO REALIZAR LA CONEXIÓN * ");
            System.out.println("******************************************");
        }

    }// FIN METODO CONECTAR
//-----------------------------------------------------------------------------------------------

 /*METODO PARA DESCONECTAR LA BBDD*/
    private void desconectar() {
        try {
            conexion.close(); // 5. Cerrar la conexión - Este método finaliza la conexión con la DB.
            System.out.println("\n************************************************************\n");
            System.out.println("La conexion a la base de datos se ha terminado");
            System.out.println("\n************************************************************");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }//desconectar
   
//----------------------------------------------------------------------------------------------   

/*MÉTODO PARA REALIZAR UNA CONSULTA A UNA TABLA MYSQL*/
        private void consultarTabla() {
        //Realizamos la consulta sql para mostrar todos los datos de la tabla pacientes y medico
        ResultSet r = buscar("select PacDni,PacNom,PacApe,PacDom from pacientes");  // Resulset devuelve el resultado de una consulta
        try {
            System.out.println("REGISTROS DE LA TABLA PACIENTES");
             /*
            Hacemos un While para recorrer toda la tabla pacientes y así poder sacar todos los registros de la tabla
            */
            while (r.next()) {
                /*Se muestra los datos que queremos sacar por consola indicandole: El tipo de dato (int,String...) de cada campo
                        El nombre de los campos de la tabla entre comillas doble " " */
                System.out.println(r.getInt("PacDni") + " | " + r.getString("PacNom") + " | " + r.getString("PacApe") + " | " + r.getString("PacDom"));// + " | " + r.getInt("EmpDep"));
            }
        } catch (SQLException ex) {
       //     Logger.getLogger(PruebaC.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ResultSet t = buscar("select MedMat,MedNom,MedApe,MedEsp from medico");  // Resulset devuelve el resultado de una consulta
        try {
            System.out.println("REGISTROS DE LA TABLA MEDICO");
             /*
            Hacemos un While para recorrer toda la tabla medico y así poder sacar todos los registros de la tabla
            */
            while (t.next()) {
                /*Se muestra los datos que queremos sacar por consola indicandole: El tipo de dato (int,String...) de cada campo
                        El nombre de los campos de la tabla entre comillas doble " " */
                System.out.println(t.getInt("MedMat") + " | " + t.getString("MedNom") + " | " + t.getString("MedApe") + " | " + t.getString("MedEsp"));// + " | " + r.getInt("EmpDep"));
            }
        } catch (SQLException ex) {
       //     Logger.getLogger(PruebaC.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//mostrarTabla
          
    //Este método lo usamos para mostrar los datos de un registro de la tabla: (executeQuery)
    ResultSet buscar(String sql) {
        try {
            sentencia = conexion.createStatement(); // 3.Este método es usado para crear la sentencia.
            //Esta sentencia es la responsable de ejecutar las consultas a la DB.
            return sentencia.executeQuery(sql);  // 4. Ejecutar consulta - Este método devuelve el resultado de una consulta (filas).
        } catch (SQLException ex) {
            Logger.getLogger(TPFinal.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }//buscar
     
    
    ////////////////////////////////////////////////////////////////////////////
    
    /*MÉTODO PARA REALIZAR UNA CARGA A LA TABLA PACIENTES MYSQL*/
        private void agregarTablaPac() {
        	String usuario="root";
            String password="Greiscol01";
            Scanner sc = new Scanner(System.in);//se crea un objeto de tipo Scanner (sc) para recibir datos en la consola
            
            System.out.println("Escriba el DNI del paciente: ");
            int PacDni  = sc.nextInt(); //asigna la entrada de usuario a la DNI
            
            System.out.println("Ingrese el nombre del paciente:  ");
            String PacNom = sc.next(); //asigna la entrada de usuario a la variable nombre
            
            System.out.println("Ingrese el apellido del paciente:  ");
            String PacApe = sc.next(); //asigna la entrada de usuario a la variable apellido
            
            System.out.println("Ingrese el domicilio del paciente:  ");
            String PacDom = sc.next(); //asigna la entrada de usuario a la variable domicilio
                    
            String sql = "insert into pacientes (PacDni,PacNom,PacApe,PacDom) values ('"+PacDni+"','"+PacNom+"','"+PacApe+"','"+PacDom+"')";
            Connection con=null;
                try{
            //En la siguiente linea se crea la conexion a la Base de datos
           con=DriverManager.getConnection("jdbc:mysql://localhost:3306/consultorio", usuario, password);  
           Statement sentencia = con.createStatement();    //3. Crear la sentencia - Este método es usado para crear la sentencia.
                                                  //Esta sentencia es la responsable de ejecutar las consultas a la DB.

           int m = sentencia.executeUpdate(sql); //Se ejecuta la instruccion sql (que paso por parametros previamente)
             if (m == 1)
                 System.out.println("Se realizo correctamente la insercion : "+sql);
             else
                 System.out.println("fallo la insercion");
          con.close();  //se cierra la conexion a la base de datos
        }
        catch(Exception e)
        {
           e.printStackTrace();
        }
    }
   
 //-----------------------------------------------------------------------------------------------------
        
        /*MÉTODO PARA REALIZAR UNA ELIMINACION A UNA TABLA PACIENTES MYSQL*/
            private void eliminarTablaPac() {
            	String usuario="root";
                String password="Greiscol01";
                Scanner sc = new Scanner(System.in);//se crea un objeto de tipo Scanner (SC) para recibir datos en la consola

                System.out.println("Escriba el DNI del paciente a eliminar:...");
                int PacDni  = sc.nextInt(); //asigna la entrada de usuario a la DNI
                
                String sql ="DELETE FROM pacientes WHERE PacDni = '"+PacDni+"'";
                Connection con=null;
                try {
                	//Class.forName("com.mysql.jdbc.Driver");     
                	con=DriverManager.getConnection("jdbc:mysql://localhost:3306/consultorio", usuario, password);  
                    Statement sentencia = conexion.createStatement();
                    sentencia.execute(sql);   
                    System.out.println("El registro se elimino!!");
                } catch (Exception e) {  
                  e.printStackTrace();
                  System.out.println("Error en el borrado del registro!!");
                }
              }
            
            private void agregarTablaMed() {
            	String usuario="root";
                String password="Greiscol01";
                Scanner sc = new Scanner(System.in);//se crea un objeto de tipo Scanner (sc) para recibir datos en la consola
                
                System.out.println("Escriba la matricula del medico: ");
                int MedMat  = sc.nextInt(); //asigna la entrada de usuario a la DNI
                
                System.out.println("Ingrese el nombre del medico:  ");
                String MedNom = sc.next(); //asigna la entrada de usuario a la variable nombre
                
                System.out.println("Ingrese el apellido del medico:  ");
                String MedApe = sc.next(); //asigna la entrada de usuario a la variable apellido
                
                System.out.println("Ingrese la especialidad del medico:  ");
                String MedEsp = sc.next(); //asigna la entrada de usuario a la variable domicilio
                        
                String sql = "insert into medico (MedMat,MedNom,MedApe,MedEsp) values ('"+MedMat+"','"+MedNom+"','"+MedApe+"','"+MedEsp+"')";
                Connection con=null;
                    try{
                //En la siguiente linea se crea la conexion a la Base de datos
               con=DriverManager.getConnection("jdbc:mysql://localhost:3306/consultorio", usuario, password);  
               Statement sentencia = con.createStatement();    //3. Crear la sentencia - Este método es usado para crear la sentencia.
                                                      //Esta sentencia es la responsable de ejecutar las consultas a la DB.

               int m = sentencia.executeUpdate(sql); //Se ejecuta la instruccion sql (que paso por parametros previamente)
                 if (m == 1)
                     System.out.println("Se realizo correctamente la insercion : "+sql);
                 else
                     System.out.println("fallo la insercion");
              con.close();  //se cierra la conexion a la base de datos
            }
            catch(Exception e)
            {
               e.printStackTrace();
            }
        }
       
     //-----------------------------------------------------------------------------------------------------
            
            /*MÉTODO PARA REALIZAR UNA ELIMINACION A UNA TABLA MEDICO MYSQL*/
                private void eliminarTablaMed() {
                	String usuario="root";
                    String password="Greiscol01";
                    Scanner sc = new Scanner(System.in);//se crea un objeto de tipo Scanner (SC) para recibir datos en la consola

                    System.out.println("Escriba la matricula del medico a eliminar:...");
                    int MedMat  = sc.nextInt(); //asigna la entrada de usuario a MedMat
                    
                    String sql ="DELETE FROM medico WHERE MedMat = '"+MedMat+"'";
                    Connection con=null;
                    try {
                    	//Class.forName("com.mysql.jdbc.Driver");     
                    	con=DriverManager.getConnection("jdbc:mysql://localhost:3306/consultorio", usuario, password);  
                        Statement sentencia = conexion.createStatement();
                        sentencia.execute(sql);   
                        System.out.println("El registro se elimino!!");
                    } catch (Exception e) {  
                      e.printStackTrace();
                      System.out.println("Error en el borrado del registro!!");
                    }
                  }
        
    
 }//FIN
