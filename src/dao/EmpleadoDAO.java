package dao;

import dto.EmpleadoDTO;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;

public class EmpleadoDAO {
    private Connection conexion;

    public EmpleadoDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public boolean insertarEmpleado(EmpleadoDTO em){
        String sql="INSERT INTO EMPLEADO (NOMBRE, CARGO, TURNO_TRABAJO, ANOS_EXPE) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps=conexion.prepareStatement(sql)) {
            ps.setString(1, em.getNombre());
            ps.setString(2, em.getCargo());
            ps.setString(3, em.getTurno_trabajo());
            ps.setInt(4, em.getAnos_expe());
            int insercion=ps.executeUpdate();
            return insercion>0;

        } catch (SQLException e) {
            System.out.println("Error al insertar el empleado: "+e.getMessage());
            return false;
        }
    }

    public boolean modificarEmpleado(EmpleadoDTO em){
        String sql="UPDATE EMPLEADO SET NOMBRE=?, CARGO=?, TURNO_TRABAJO=?, ANOS_EXPE=? WHERE ID=?";
        try (PreparedStatement ps= conexion.prepareStatement(sql)) {
            ps.setString(1, em.getNombre());
            ps.setString(2, em.getCargo());
            ps.setString(3, em.getTurno_trabajo());
            ps.setInt(4, em.getAnos_expe());
            ps.setInt(5, em.getId());
            int modifi=ps.executeUpdate();
            return modifi>0;
        } catch (SQLException e) {
            System.out.println("Error al modificar empleado: "+e.getMessage());
            return false;
        }

    }

    public EmpleadoDTO buscarPorId(int id){
        String sql="SELECT * FROM EMPLEADO WHERE ID=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
           try (ResultSet rs=ps.executeQuery()) {
            if(rs.next()){
                EmpleadoDTO empleado= new EmpleadoDTO();
                empleado.setId(rs.getInt("ID"));
                empleado.setNombre(rs.getString("NOMBRE"));
                empleado.setCargo(rs.getString("CARGO"));
                empleado.setTurno_trabajo(rs.getString("TURNO_TRABAJO"));
                empleado.setAnos_expe(rs.getInt("ANOS_EXPE"));
                return empleado;
            }
           }
        } catch (SQLException e) {
            System.out.println("Error al encontrar el empleado: "+e.getMessage());
        }
        return null;
    }

    public ArrayList<EmpleadoDTO> listarEmpleados(){
        ArrayList<EmpleadoDTO> empleados=new ArrayList<>();
        String sql="SELECT * FROM EMPLEADO";
        try (PreparedStatement ps= conexion.prepareStatement(sql);
            ResultSet rs= ps.executeQuery()) {
            while(rs.next()){
                EmpleadoDTO emp= new EmpleadoDTO();
                emp.setId(rs.getInt("ID"));
                emp.setNombre(rs.getString("NOMBRE"));
                emp.setCargo(rs.getString("CARGO"));
                emp.setAnos_expe(rs.getInt("ANOS_EXPE"));
                empleados.add(emp);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar los empleados: "+e.getMessage());
        }
        return empleados;
    }

    public double calcularComision(int id, LocalDate inicio, LocalDate fin){
        double comision=0.00;
        String sql="{? = call calculo_comision_ventas(?, ?, ?)}";
        try (CallableStatement cs = conexion.prepareCall(sql)) {
            cs.registerOutParameter(1, Types.DOUBLE);
            cs.setInt(2, id);
            cs.setDate(3, Date.valueOf(inicio));
            cs.setDate(4, Date.valueOf(fin));
            try (ResultSet rs=cs.executeQuery()) {
                if(rs.next()){
                    comision=rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al calcular la comisión: "+e.getMessage());
        }
        return comision;
    }

    
}
