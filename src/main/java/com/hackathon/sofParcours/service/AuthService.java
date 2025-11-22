package com.hackathon.sofParcours.service;

import com.hackathon.sofParcours.model.User;
import com.hackathon.sofParcours.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final SecretKey secretKey;
    
    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;

    public AuthService(UserRepository userRepository,
                      @Value("${jwt.secret:myVerySecureAndLongSecretKeyForHS512AlgorithmThatIsAtLeast64CharactersLongToMeetTheRequirements123456}") String jwtSecret) {
        this.userRepository = userRepository;
        // Créer une clé sécurisée à partir de la chaîne
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Inscription d'un nouvel utilisateur
     */
    public User register(String username, String email, String password) {
        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Vérifier si le username existe déjà
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Créer le nouvel utilisateur
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(password); // Assurez-vous de hacher le mot de passe correctement

        return userRepository.save(user);
    }

    /**
     * Connexion utilisateur
     */
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        // Vérifier le mot de passe avec passwordHash
        

        // Générer et retourner le token JWT
        return generateToken(email);
    }

    /**
     * Générer un token JWT
     */
    private String generateToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(secretKey, SignatureAlgorithm.HS512)
            .compact();
    }

    
   
    /**
     * Trouver un utilisateur par email
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    /**
     * Trouver un utilisateur par ID
     */
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    /**
     * Obtenir un utilisateur par ID (pour AuthController)
     */
    public User getUserById(String id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}