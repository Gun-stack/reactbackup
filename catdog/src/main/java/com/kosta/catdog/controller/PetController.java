package com.kosta.catdog.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.catdog.entity.Pet;
import com.kosta.catdog.repository.PetRepository;
import com.kosta.catdog.repository.UserDslRepository;
import com.kosta.catdog.service.PetService;

@RestController
public class PetController {
	@Autowired
	private PetService petService;
	@Autowired 
	private UserDslRepository userDslRepository;
	@Autowired
	private PetRepository 	petRepository;
	
	
	@PostMapping("/petreg")
	public ResponseEntity<Boolean> petreg (
			@RequestPart(value="file", required = false) List<MultipartFile> file,
			@RequestParam("name") String name,
			@RequestParam("dogOrCat") Boolean dogOrCat,
			@RequestParam("age") Integer age,
			@RequestParam("weight") String weight,
			@RequestParam("breed") String breed,
			@RequestParam("gender") Boolean gender,
			@RequestParam("neuter") Boolean neuter,
			@RequestParam("userNum") Integer usernum
			) 
	{
		try {
			Pet pet =new Pet();
				pet.setName(name);
		        pet.setDogOrCat(dogOrCat);  // Corrected type casting
		        pet.setAge(age);  // Corrected type casting
		        pet.setWeight(new BigDecimal(weight));
		        pet.setBreed(breed);
		        pet.setGender(gender);
		        pet.setNeuter(neuter);
		        pet.setUserNum(usernum);
		        System.out.println(pet);
		        System.out.println(file);
		        
		        if (file != null && !file.isEmpty()) {
		            petService.petReg(pet, file);
		        } else {
		            petService.petReg(pet, null);  // 또는 파일을 처리하지 않는 메서드 호출
		        }
			
			return new ResponseEntity<Boolean>(true,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Boolean>(false,HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/petimg/{num}")
	public void ImageView(@PathVariable Integer num, HttpServletResponse response) {
		try {
			petService.fileView(num, response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// 등록한 반려동물 조회
	@GetMapping("/petinfo")
	public ResponseEntity<List<Pet>> selectPets(@RequestParam String userId){
		System.out.println("petInfo : id :"+ userId);
		List<Pet> petList= userDslRepository.findPetsByUserID(userId);
		System.out.println(petList);
		
		try {
			return new ResponseEntity<List<Pet>> (petList , HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<Pet>> (HttpStatus.BAD_REQUEST);
		}
	}
		
		@GetMapping("/petinfobyidandname")
		public ResponseEntity<Pet> selectPetByUserIdAndPetName(@RequestParam Integer userNum , String petName){
			System.out.println();
				Pet pet = userDslRepository.FindPetByuserIdAndPetName(userNum, petName);
			
			try {
				return new ResponseEntity<Pet> (pet , HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<Pet> (HttpStatus.BAD_REQUEST);
			}
		
	}
	// 등록한 반려동물 조회
		@GetMapping("/petinfobynum")
		public ResponseEntity<Pet> selectPetByNum(@RequestParam Integer num){
			Pet pet = petRepository.findById(num).get();
			
			try {
				return new ResponseEntity<Pet> (pet , HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<Pet> (HttpStatus.BAD_REQUEST);
			}
			
		}
			

	@PostMapping("/petmodi")
	public ResponseEntity<Boolean> modipet(
			@RequestPart(value="file", required = false) List<MultipartFile> file,
			@RequestParam("num") Integer num,
			@RequestParam("name") String name,
			@RequestParam("dogOrCat") Boolean dogOrCat,
			@RequestParam("age") Integer age,
			@RequestParam("weight") String weight,
			@RequestParam("breed") String breed,
			@RequestParam("gender") Boolean gender,
			@RequestParam("neuter") Boolean neuter,
			@RequestParam("userNum") Integer usernum
			){
		try {
			Pet pet =new Pet();
			pet.setNum(num);
			pet.setName(name);
	        pet.setDogOrCat(dogOrCat);  // Corrected type casting
	        pet.setAge(age);  // Corrected type casting
	        pet.setWeight(new BigDecimal(weight));
	        pet.setBreed(breed);
	        pet.setGender(gender);
	        pet.setNeuter(neuter);
	        pet.setUserNum(usernum);
	        System.out.println(pet);
	        System.out.println(file);
	        if (file != null && !file.isEmpty()) {
	            petService.petModi(pet, file);
	        } else {
	            petService.petModi(pet, null);  // 또는 파일을 처리하지 않는 메서드 호출
	        }
		        
		       
			return new ResponseEntity<Boolean>(true,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Boolean>(false,HttpStatus.BAD_REQUEST);
		}
	}
	
	@Transactional
	@PostMapping("/petdelete")
	public ResponseEntity<Boolean> deletePet(@RequestParam Integer num)
	{
		try {
			Pet pet  = petRepository.findById(num).get();
			petRepository.delete(pet);
			return new ResponseEntity<Boolean>(true,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
		}
	
	}

}
