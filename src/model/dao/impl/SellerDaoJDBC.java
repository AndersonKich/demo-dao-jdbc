package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection conn; // conexão com o BD
	
	
	public SellerDaoJDBC(Connection conn) {//Construtor
		this.conn = conn;
	}

	@Override
	public void insertSeller(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateSeller(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(//Implementação do comando sql de busca pelo id
					"SELECT seller.*,department.Name as DepName "
					+"FROM seller INNER JOIN department "
					+"ON seller.DepartmentId = department.Id "
					+"WHERE seller.Id = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			if(rs.next()) {
				Department dep = InstanciateDepartment(rs);//Função auxiliar criado
				
				Seller sel = InstanciateSeller(rs,dep);//Função auxiliar criado
				return sel;
			}
			return null;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
		
		
		
	
	}
	//Função auxiliar de Department
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

	//Função auxiliar de Department
	private Department InstanciateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();//Convertendo a tabela DB em objeto
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> finAll() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
	
	
	
}
