package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Statement;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection conn; // conexão com o BD

	public SellerDaoJDBC(Connection conn) {// Construtor
		this.conn = conn;
	}

	@Override
	public void insertSeller(Seller sell) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
			"INSERT INTO seller "
			+"(Name, Email, BirthDate, BaseSalary, DepartmentId) "
			+"VALUES "
			+"(?, ?, ?, ?, ?)",
			Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, sell.getName());
			st.setString(2, sell.getEmail());
			st.setDate(3, new java.sql.Date(sell.getBirthDate().getTime()));//Inseindo Date no DB
			st.setDouble(4, sell.getBaseSalary());
			st.setInt(5, sell.getDepartment().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					sell.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Erro inesperado! Nenhuma linha afetada!");
			}
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void updateSeller(Seller sell) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE seller "
					+"SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+"WHERE Id = ?");
			
			st.setString(1, sell.getName());
			st.setString(2, sell.getEmail());
			st.setDate(3, new java.sql.Date(sell.getBirthDate().getTime()));//Inseindo Date no DB
			st.setDouble(4, sell.getBaseSalary());
			st.setInt(5, sell.getDepartment().getId());
			st.setInt(6, sell.getId());
			
			st.executeUpdate();
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM seller WHERE Id = ?");
			
			st.setInt(1, id);
			st.executeUpdate();
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Seller findById(Integer id) {

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(// Implementação do comando sql de busca pelo id
					"SELECT seller.*,department.Name as DepName " 
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Department dep = InstanciateDepartment(rs);// Função auxiliar criado

				Seller sel = InstanciateSeller(rs, dep);// Função auxiliar criado
				return sel;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}

	}

	// Função auxiliar de Department
	private Seller InstanciateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller sel = new Seller();
		sel.setId(rs.getInt("Id"));
		sel.setName(rs.getString("Name"));
		sel.setEmail(rs.getString("Email"));
		sel.setBirthDate(rs.getDate("BirthDate"));
		sel.setBaseSalary(rs.getDouble("BaseSalary"));
		sel.setDepartment(dep);
		return sel;
	}

	// Função auxiliar de Department
	private Department InstanciateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();// Convertendo a tabela DB em objeto
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> finAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(//Implementação do comando sql de busca pelo id
					"SELECT seller.*,department.Name as DepName "
					+"FROM seller INNER JOIN department "
					+"ON seller.DepartmentId = department.Id "
					+"ORDER BY Name");
					
			rs = st.executeQuery();
			
			List<Seller>seler = new ArrayList<Seller>();
			Map<Integer, Department> map = new HashMap<>();
			
			while (rs.next()){
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if(dep == null) {
					
				 dep = InstanciateDepartment(rs);//Função auxiliar criado 	
				 map.put(rs.getInt("DepartmentId"), dep);
					
				}
				Seller sel = InstanciateSeller(rs,dep);//Função auxiliar criado
				seler.add(sel);
			}
			return seler;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(//Implementação do comando sql de busca pelo id
					"SELECT seller.*,department.Name as DepName " 
					+"FROM seller INNER JOIN department "
					+"ON seller.DepartmentId = department.Id "
					+"WHERE DepartmentId = ? "
					+"ORDER BY Name");
					
			st.setInt(1, department.getId());
			rs = st.executeQuery();
			
			List<Seller>seler = new ArrayList<Seller>();
			Map<Integer, Department> map = new HashMap<>();
			
			while (rs.next()){
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if(dep == null) {
					
				 dep = InstanciateDepartment(rs);//Função auxiliar criado 	
				 map.put(rs.getInt("DepartmentId"), dep);
					
				}
				Seller sel = InstanciateSeller(rs,dep);//Função auxiliar criado
				seler.add(sel);
			}
			return seler;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
		
	}

}
