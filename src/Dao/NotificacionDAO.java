package Dao;

import Enums.Contexto;
import Enums.Contenido;
import Model.Notificacion;
import Utils.ConexionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificacionDAO extends ObservableDAO<Notificacion>{
    public void guardarNotificacion(Notificacion notificacion){
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            String query = prepareInsert(notificacion);
            Connection conexion = ConexionManager.getConexion();
            Statement statement = conexion.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String prepareInsert(Notificacion notificacion){
        return "INSERT INTO NOTIFICACION" +
                " (contenido, contexto, categoria_id, fecha_envio, fecha_recepcion, nino_id" +
                " VALUES (" + notificacion.getContenido().toString() + "," + notificacion.getContexto().toString() + "," +
                notificacion.getCategoria().getId() + "," + notificacion.getFechaEnvio() + "," +
                notificacion.getFechaRecepcion() + "," + notificacion.getNene().getId() + ")";
    }

    public Notificacion getNotificacion(Long id){
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            String query = prepareGet(id);
            Connection conexion = ConexionManager.getConexion();
            Statement statement = conexion.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return new Notificacion(resultSet.getLong("id"),Contenido.valueOf(resultSet.getString("contenido")), Contexto.valueOf(String.valueOf(resultSet.getInt("contexto"))),resultSet.getLong("categoria_id"),resultSet.getDate("fecha_envio"),resultSet.getDate("fecha_recepcion"),resultSet.getLong("nino_id"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Notificacion> getNotificacionesFiltradas(Contenido contenido,Contexto contexto,Long nene_id,Long categoria_id, Long etiqueta_id,Date desde,Date hasta){
        List<Notificacion> lista = new ArrayList<>();
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            String query = prepareFilteredList(contenido, contexto, nene_id, categoria_id, etiqueta_id, desde, hasta);
            Connection conexion = ConexionManager.getConexion();
            Statement statement = conexion.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                lista.add(new Notificacion(resultSet.getLong("id"),Contenido.valueOf(resultSet.getString("contenido")), Contexto.valueOf(String.valueOf(resultSet.getInt("contexto"))),resultSet.getLong("categoria_id"),resultSet.getDate("fecha_envio"),resultSet.getDate("fecha_recepcion"),resultSet.getLong("nino_id")));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private String prepareFilteredList(Contenido contenido,Contexto contexto,Long nene_id,Long categoria_id, Long etiqueta_id,Date desde,Date hasta){
        Boolean first = true;
        StringBuilder sb = new StringBuilder("SELECT * FROM NOTIFICACION");
        if (contenido != null || contexto != null || nene_id != null || categoria_id != null || etiqueta_id != null || desde != null || hasta != null) {
            sb.append(" WHERE");
            if (contenido != null) {
                sb.append(" contenido = ").append(contenido.toString()).append("'");
                first = false;
            }
            if (contexto != null) {
                if (!first){
                    sb.append(" & ");
                }
                sb.append(" contexto='").append(contexto.toString()).append("'");
                first = false;
            }
            if (nene_id != null) {
                if (!first){
                    sb.append(" & ");
                }
                sb.append(" nino_id =").append(nene_id.toString());
                first = false;
            }
            if (categoria_id != null) {
                if (!first){
                    sb.append(" & ");
                }
                sb.append(" categoria_id =").append(categoria_id.toString());
                first = false;
            }
            if (etiqueta_id != null) {
                if (!first){
                    sb.append(" & ");
                }
                sb.append(" etiqueta_id =").append(etiqueta_id.toString());
                first = false;
            }
            if (desde != null) {
                if (!first){
                    sb.append(" & ");
                }
                sb.append(" fecha_envio > '").append(desde.toString()).append("'");
                first = false;
            }
            if (hasta != null) {
                if (!first){
                    sb.append(" & ");
                }
                sb.append(" fecha_envio < '").append(hasta.toString()).append("'");
            }
        }
        return sb.toString();
    }

    private String prepareGet(Long id){
        return "SELECT * FROM NOTIFICACION WHERE ID="+id;
    }

    @Override
    public List<Notificacion> getAllItems() {
        return null;
    }
}
