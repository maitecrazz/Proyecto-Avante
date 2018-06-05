package pepe.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import pepe.connection.DatabaseConnectionFactory;
import pepe.model.Publicacion;

public class PublicacionDAO {

	public static void createPublicacion(Publicacion publicacion) throws SQLException {
		// get connection from connection pool
		Connection con = DatabaseConnectionFactory.getConnectionFactory().getConnection();
		try {
			final String sql = "insert into Publicacion (titulo,descripcion,etiqueta,fecha) values (?,?,?,?)";
			// create the prepared statement with an option to get auto-generated keys
			PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			// set parameters
			stmt.setString(1, publicacion.getTitulo());
			stmt.setString(2, publicacion.getDescripcion());
			stmt.setString(3, publicacion.getEtiqueta());
			stmt.setDate(4, publicacion.getFecha());

			stmt.execute();

			// Get auto-generated keys
			ResultSet rs = stmt.getGeneratedKeys();

			if (rs.next())
				publicacion.setId(rs.getInt(1));

			rs.close();
			stmt.close();
		} finally {
			con.close();
		}
	}

	public static void removePublicacion(int publicacionId) throws SQLException {
		Connection con = DatabaseConnectionFactory.getConnectionFactory().getConnection();
		try {
			final String sql = "delete from Publicacion where Id= (?) ";

			PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, publicacionId);
			stmt.execute();

			stmt.close();
		} finally {
			con.close();
		}

	}

	public static void editPublicacion(Publicacion publicacion) throws SQLException {
		Connection con = DatabaseConnectionFactory.getConnectionFactory().getConnection();
		try {
			final String sql = "edit from Publicacion where Id= (?) ";

			PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			stmt.setInt(1, publicacion.getId());
			stmt.setString(2, publicacion.getTitulo());
			stmt.setString(3, publicacion.getDescripcion());
			stmt.setString(4, publicacion.getEtiqueta());
			stmt.setDate(5, publicacion.getFecha());

			stmt.execute();

		} finally {
			con.close();
		}

	}

	public List<Publicacion> getPublicaciones() throws SQLException {
		// get connection from connection pool
		Connection con = DatabaseConnectionFactory.getConnectionFactory().getConnection();

		List<Publicacion> publicacion = new ArrayList<Publicacion>();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();

			// create SQL statement using left outer join
			StringBuilder sb = new StringBuilder("select Publicacion.id as id, Publicacion.titulo as titulo,").append(
					"Publicacion.descripcion as descripcion, Publicacion.etiqueta as etiqueta, Publicacion.fecha as fecha, Publicacion.valoracion as valoracion, Publicacion.id_usuario as id_usuario ")
					.append("from Publicacion left outer join Usuario on ")
					.append("Publicacion.id_usuario = Usuario.id");

			// execute the query
			rs = stmt.executeQuery(sb.toString());

			// iterate over result set and create Course objects
			// add them to course list
			while (rs.next()) {
				Publicacion publi = new Publicacion();
				publi.setId(rs.getInt("id"));
				publi.setTitulo(rs.getString("titulo"));
				publi.setDescripcion(rs.getString("descripcion"));
				publi.setEtiqueta(rs.getString("etiqueta"));
				publi.setFecha(rs.getDate("fecha"));
				publi.setValoracion(rs.getInt("valoracion"));
				publicacion.add(publi);

				
			}

			return publicacion;
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
			}
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
			}
			try {
				con.close();
			} catch (SQLException e) {
			}
		}
	}

}