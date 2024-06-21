package com.fundingflex.funding.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fundingflex.dto.FundingRequestDTO;
import com.fundingflex.dto.FundingResponseDTO;
import com.fundingflex.funding.domain.entity.FundingConditions;
import com.fundingflex.funding.service.FundingJoinService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class FundingJoinController {

	private final FundingJoinService fundingJoinService;

	
	@PostMapping("/api/fundings/join")
	@ResponseBody
	public ResponseEntity<Object> joinFunding(@RequestBody FundingRequestDTO requestDto) {
		try {
			FundingResponseDTO responseDto = fundingJoinService.joinFunding(requestDto);
			return ResponseEntity.ok(responseDto);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(400).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Failed to join funding: " + e.getMessage());
		}
	}

	@PutMapping("/api/fundings/update")
	@ResponseBody
	public ResponseEntity<Object> updateFunding(@RequestBody FundingRequestDTO requestDto) {
		try {
			FundingResponseDTO responseDto = fundingJoinService.updateFunding(requestDto);
			return ResponseEntity.ok(responseDto);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(400).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

	@DeleteMapping("/api/fundings/delete/{fundingJoinId}")
	@ResponseBody
	public ResponseEntity<Object> deleteFunding(@PathVariable("fundingJoinId") Long fundingJoinId) {
		try {
			fundingJoinService.deleteFunding(fundingJoinId);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

	@GetMapping("/fundingJoin")
	public String getFundingJoinPage() {
		return "fundingJoin";
	}

	@GetMapping("/api/fundings/conditions/{fundingsId}")
	@ResponseBody
	public ResponseEntity<Object> getFundingConditions(@PathVariable Long fundingsId) {
		Optional<FundingConditions> fundingConditions = fundingJoinService.getFundingConditions(fundingsId);
		if (fundingConditions.isPresent()) {
			return ResponseEntity.ok(fundingConditions.get());
		} else {
			return ResponseEntity.status(404).body("Funding conditions not found");
		}
	}
}

/*
 * package com.fundingflex.controller;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.http.ResponseEntity; import
 * org.springframework.stereotype.Controller; import
 * org.springframework.web.bind.annotation.DeleteMapping; import
 * org.springframework.web.bind.annotation.GetMapping; import
 * org.springframework.web.bind.annotation.PathVariable; import
 * org.springframework.web.bind.annotation.PostMapping; import
 * org.springframework.web.bind.annotation.PutMapping; import
 * org.springframework.web.bind.annotation.RequestBody; import
 * org.springframework.web.bind.annotation.ResponseBody;
 * 
 * import com.fundingflex.dto.FundingRequestDTO; import
 * com.fundingflex.dto.FundingResponseDTO; import
 * com.fundingflex.service.FundingJoinService;
 * 
 * @Controller public class FundingJoinController {
 * 
 * kotlin 코드 복사 private final FundingJoinService fundingJoinService;
 * 
 * @Autowired public FundingJoinController(FundingJoinService
 * fundingJoinService) { this.fundingJoinService = fundingJoinService; }
 * 
 * @PostMapping("/api/fundings/join")
 * 
 * @ResponseBody public ResponseEntity<Object> joinFunding(@RequestBody
 * FundingRequestDTO requestDto) { try { FundingResponseDTO responseDto =
 * fundingJoinService.joinFunding(requestDto); return
 * ResponseEntity.ok(responseDto); } catch (IllegalArgumentException e) { return
 * ResponseEntity.status(400).body(e.getMessage()); } catch (Exception e) {
 * return ResponseEntity.status(500).body("Failed to join funding: " +
 * e.getMessage()); } }
 * 
 * @PutMapping("/api/fundings/update")
 * 
 * @ResponseBody public ResponseEntity<Object> updateFunding(@RequestBody
 * FundingRequestDTO requestDto) { try { FundingResponseDTO responseDto =
 * fundingJoinService.updateFunding(requestDto); return
 * ResponseEntity.ok(responseDto); } catch (Exception e) { return
 * ResponseEntity.status(400).body(e.getMessage()); } }
 * 
 * @DeleteMapping("/api/fundings/delete/{fundingJoinId}")
 * 
 * @ResponseBody public ResponseEntity<Object>
 * deleteFunding(@PathVariable("fundingJoinId") Long fundingJoinId) { try {
 * fundingJoinService.deleteFunding(fundingJoinId); return
 * ResponseEntity.ok().build(); } catch (Exception e) { return
 * ResponseEntity.status(400).body(e.getMessage()); } }
 * 
 * @GetMapping("/fundingJoin") public String getFundingJoinPage() { return
 * "fundingJoin"; } }
 */