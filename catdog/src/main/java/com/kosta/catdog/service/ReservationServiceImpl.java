package com.kosta.catdog.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.catdog.entity.PetFileVO;
import com.kosta.catdog.entity.ResFileVO;
import com.kosta.catdog.entity.Reservation;
import com.kosta.catdog.repository.ResFileVORepository;
import com.kosta.catdog.repository.ReservationRepository;
import com.kosta.catdog.repository.UserDslRepository;


@Service
public class ReservationServiceImpl implements ReservationService {

	@Autowired
	private UserDslRepository userDslRepository;
	@Autowired
	private ReservationRepository reservationRepository;
	@Autowired
	private ResFileVORepository fileVORepository;
	
	@Override
	public Reservation makeReservation(Reservation reservation ) throws Exception {
		
	
		reservationRepository.save(reservation);
		return reservation;
		
	}
	
	@Value("${res.upload.dir}")
	private String uploadDir; // 파일이 업로드 되는dir
	
	@Override
	public Reservation findReservation(Integer num) throws Exception {
		return reservationRepository.findReservationByNum(num);
	}
	
	@Override
	public List<Reservation> reservationListByDesignerAndDate(Integer num, Date date) throws Exception {
		return userDslRepository.findReservationListByDesigner_AndDate(num, date);
	}

	@Override
	public void modifyReservation(Reservation reservation) throws Exception {
		reservationRepository.save(reservation);
	}

	@Override
	public void cancelReservation(Integer num) throws Exception {
		reservationRepository.deleteById(num);
	}
	
	@Override
	public void CompleteReservation(Reservation res, MultipartFile file) throws Exception {
		
		if (file != null) {
			String fileNums = "";
			Date today = Date.valueOf(LocalDate.now());
			ResFileVO fileVO = new ResFileVO();
			fileVO.setDir(uploadDir);
			fileVO.setName(file.getOriginalFilename());
			fileVO.setSize(file.getSize());
			fileVO.setType(file.getContentType());
			fileVO.setDate(today);
			fileVORepository.save(fileVO);

			File uploadFile = new File(uploadDir + fileVO.getNum());
			file.transferTo(uploadFile);
			if (!fileNums.equals(""))
				fileNums += ",";
			fileNums += fileVO.getNum();
			res.setCompleteImg(fileNums);
		}
		reservationRepository.save(res);
		
	}

	@Override
	public Reservation addReservationFile(Reservation res, MultipartFile file) throws Exception {
		
		if (file != null) {
			String fileNums = "";
			Date today = Date.valueOf(LocalDate.now());
			ResFileVO fileVO = new ResFileVO();
			fileVO.setDir(uploadDir);
			fileVO.setName(file.getOriginalFilename());
			fileVO.setSize(file.getSize());
			fileVO.setType(file.getContentType());
			fileVO.setDate(today);
			fileVORepository.save(fileVO);

			File uploadFile = new File(uploadDir + fileVO.getNum());
			file.transferTo(uploadFile);
			if (!fileNums.equals(""))
				fileNums += ",";
			fileNums += fileVO.getNum();
			res.setRefImg(fileNums);
		}
		reservationRepository.save(res);
		
		
		
		return res;
	}
	
	@Override
	public void fileView(Integer num, OutputStream out) throws Exception {
		try {
			Optional<ResFileVO> fileVoOptional  = fileVORepository.findById(num);
			ResFileVO fileVo = fileVoOptional.get();
//			FileCopyUtils.copy(fileVo.getData(), out); //데이타 뿌려주기
			FileInputStream fis = new FileInputStream(fileVo.getDir()+num);//폴더에서 가져오기 
			FileCopyUtils.copy(fis, out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
