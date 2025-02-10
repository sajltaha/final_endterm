package RAR;

import Objects.Animal;
import DBConnection.DBConnection_inner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnimalRAR {

    public void addAnimal(Animal animal) throws SQLException {
        String sql = "INSERT INTO animal (cage_id, name, is_predator) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = DBConnection_inner.getConnection().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, animal.getCageId());
            pstmt.setString(2, animal.getName());
            pstmt.setBoolean(3, animal.isPredator());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    animal.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating animal failed, no ID obtained.");
                }
            }
        }
    }

    public void updateAnimal(Animal animal) throws SQLException {
        String sql = "UPDATE animal SET name = ?, is_predator = ? WHERE id = ?";
        try (PreparedStatement pstmt = DBConnection_inner.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, animal.getName());
            pstmt.setBoolean(2, animal.isPredator());
            pstmt.setInt(3, animal.getId());
            pstmt.executeUpdate();
        }
    }

    public List<Animal> getAllAnimals() throws SQLException {
        List<Animal> animals = new ArrayList<>();
        String sql = "SELECT * FROM animal";
        try (PreparedStatement pstmt = DBConnection_inner.getConnection().prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                animals.add(new Animal(rs.getInt("cage_id"), rs.getString("name"), rs.getBoolean("is_predator"),
                        rs.getInt("id")));
            }
        }
        return animals;
    }

    public void deleteAnimal(int animalId) throws SQLException {
        String sql = "DELETE FROM animal WHERE id = ?";
        try (PreparedStatement pstmt = DBConnection_inner.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, animalId);
            pstmt.executeUpdate();
        }
    }

    public List<Animal> getAnimalsByCageId(int cageId) throws SQLException {
        List<Animal> animals = new ArrayList<>();
        String sql = "SELECT * FROM animal WHERE cage_id = ?";
        try (PreparedStatement pstmt = DBConnection_inner.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, cageId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    animals.add(new Animal(rs.getInt("cage_id"), rs.getString("name"), rs.getBoolean("is_predator"),
                            rs.getInt("id")));
                }
            }
        }
        return animals;
    }

    public List<Animal> searchAnimalsByNameAndCage(String name, int cageId) throws SQLException {
        List<Animal> animals = new ArrayList<>();
        String sql = "SELECT * FROM animal WHERE cage_id = ? AND name LIKE ?";
        try (PreparedStatement pstmt = DBConnection_inner.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, cageId);
            pstmt.setString(2, "%" + name + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    animals.add(new Animal(rs.getInt("cage_id"), rs.getString("name"), rs.getBoolean("is_predator"),
                            rs.getInt("id")));
                }
            }
        }
        return animals;
    }
}