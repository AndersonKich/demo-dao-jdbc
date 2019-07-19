package aplication;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		SellerDao sellerdao = DaoFactory.creatSellerDao();
		
		
		
		List<Seller>list = sellerdao.finAll();
		
		list.forEach(System.out::println);
		
	}

}
