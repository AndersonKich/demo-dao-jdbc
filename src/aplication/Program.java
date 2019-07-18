package aplication;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		SellerDao sellerdao = DaoFactory.creatSellerDao();
		
		Seller seller = sellerdao.findById(3);
		System.out.println(seller);
		System.out.println();
		
		List<Seller>list = sellerdao.findByDepartment(new Department(2,null));
		
		list.forEach(System.out::println);
		
	}

}
