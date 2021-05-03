package com.sanchoo.property.management.service.impl;


import com.sanchoo.property.management.dto.PasswordDto;
import com.sanchoo.property.management.entity.user.Role;
import com.sanchoo.property.management.entity.user.User;
import com.sanchoo.property.management.exception.IncorrectPasswordException;
import com.sanchoo.property.management.exception.PasswordsNotMatchException;
import com.sanchoo.property.management.exception.UserAlreadyExistsException;
import com.sanchoo.property.management.repository.RoleRepository;
import com.sanchoo.property.management.repository.UserRepository;
import com.sanchoo.property.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private SessionRegistry sessionRegistry;

	@Override
	public User findUserByUserName(String userName) {
		return this.userRepository.findByUserName(userName);
	}

	@Override
	public User saveUser(User user) throws UserAlreadyExistsException {
		User userExists = this.userRepository.findByUserName(user.getUserName());

		if(userExists != null) {
			throw new UserAlreadyExistsException("Пользователь с таким именем уже существует!");
		}

		user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
		user.setActive(true);
		user.setFileName("noname.png");
		Role role = this.roleRepository.findByRole("ROLE_USER");
		user.setRoles(new HashSet<>(List.of(role)));
		return this.userRepository.save(user);
	}

	@Override
	public User updateUser(User user) {
		return this.userRepository.save(user);
	}

	@Override
	public User getById(int id) {
		return this.userRepository.getOne(id);
	}

	@Override
	public void setNewPassword(PasswordDto passwordDto) throws PasswordsNotMatchException, IncorrectPasswordException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = findUserByUserName(auth.getName());

		boolean isPasswordEquals = this.bCryptPasswordEncoder.matches(
				passwordDto.getOldPassword(),
				user.getPassword());

		if(!isPasswordEquals) {
			throw new IncorrectPasswordException("Вы ввели неверный пароль!");
		}

		if(!passwordDto.getNewPassword().equals(passwordDto.getMatchingPassword())) {
			throw new PasswordsNotMatchException("Пароли не совпадают!");
		}

		user.setPassword(bCryptPasswordEncoder.encode(passwordDto.getNewPassword()));
		updateUser(user);
	}

	@Override
	public Page<User> findPaginatedAllUsers(Pageable pageable) {
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		List<User> users = this.userRepository.findByActive(true);
		List<User> resultList;

		if(users.size() < startItem) {
			resultList = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, users.size());
			resultList = users.subList(startItem, toIndex);
		}

		return new PageImpl<>(resultList, PageRequest.of(currentPage, pageSize), users.size());
	}

	@Override
	public Page<User> findPaginatedAllUsers(Pageable pageable, String search) {
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		List<User> users = this.userRepository.findByActive(true);
		List<User> usersSearch = this.userRepository.findBySearch(search);
		users.retainAll(usersSearch);

		List<User> resultList;

		if(users.size() < startItem) {
			resultList = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, users.size());
			resultList = users.subList(startItem, toIndex);
		}

		return new PageImpl<>(resultList, PageRequest.of(currentPage, pageSize), users.size());
	}

	@Override
	public Page<User> findPaginatedModerators(Pageable pageable) {
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		Role role = this.roleRepository.findByRole("ROLE_MODERATOR");
		List<User> users = this.userRepository.findByRolesContainsAndActive(role, true);
		List<User> resultList;

		if(users.size() < startItem) {
			resultList = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, users.size());
			resultList = users.subList(startItem, toIndex);
		}

		return new PageImpl<>(resultList, PageRequest.of(currentPage, pageSize), users.size());
	}

	@Override
	public Page<User> findPaginatedModerators(Pageable pageable, String search) {
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		Role role = this.roleRepository.findByRole("ROLE_MODERATOR");
		List<User> users = this.userRepository.findByRolesContainsAndActive(role, true);
		List<User> usersSearch = this.userRepository.findBySearch(search);
		users.retainAll(usersSearch);

		List<User> resultList;

		if(users.size() < startItem) {
			resultList = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, users.size());
			resultList = users.subList(startItem, toIndex);
		}

		return new PageImpl<>(resultList, PageRequest.of(currentPage, pageSize), users.size());
	}

	@Override
	public Page<User> findPaginatedBlockedUsers(Pageable pageable) {
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		List<User> users = this.userRepository.findByActive(false);
		List<User> resultList;

		if(users.size() < startItem) {
			resultList = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, users.size());
			resultList = users.subList(startItem, toIndex);
		}

		return new PageImpl<>(resultList, PageRequest.of(currentPage, pageSize), users.size());
	}

	@Override
	public Page<User> findPaginatedBlockedUsers(Pageable pageable, String search) {
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		List<User> users = this.userRepository.findByActive(false);
		List<User> usersSearch = this.userRepository.findBySearch(search);
		users.retainAll(usersSearch);

		List<User> resultList;

		if(users.size() < startItem) {
			resultList = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, users.size());
			resultList = users.subList(startItem, toIndex);
		}

		return new PageImpl<>(resultList, PageRequest.of(currentPage, pageSize), users.size());
	}

	@Override
	public void blockUser(int id) {
		Optional<User> userOptional = this.userRepository.findById(id);
		userOptional.ifPresent(user -> {
			user.setActive(false);
			Optional<UserDetails> userDetailsOptional = this.sessionRegistry.getAllPrincipals().stream()
					.filter(u -> !sessionRegistry.getAllSessions(u, false).isEmpty())
					.map(u -> (UserDetails) u)
					.filter(u -> u.getUsername().equals(user.getUserName()))
					.findAny();

			userDetailsOptional.ifPresent(userDetails -> {
				List<SessionInformation> allSessions = this.sessionRegistry.getAllSessions(userDetails, false);
				allSessions.forEach(SessionInformation::expireNow);
			});
		});

	}

	@Override
	public void unblockUser(int id) {
		Optional<User> userOptional = this.userRepository.findById(id);
		userOptional.ifPresent(user -> user.setActive(true));
	}

	@Override
	public void changeRoleToUser(int id) {
		Optional<User> userOptional = this.userRepository.findById(id);
		Role role = this.roleRepository.findByRole("ROLE_MODERATOR");
		userOptional.ifPresent(user -> user.getRoles().remove(role));
	}

	@Override
	public void changeRoleToModerator(int id) {
		Optional<User> userOptional = this.userRepository.findById(id);
		Role role = this.roleRepository.findByRole("ROLE_MODERATOR");
		userOptional.ifPresent(user -> user.getRoles().add(role));
	}
}
