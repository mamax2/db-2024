package db_lab.data;

import db_lab.data.DAOException;
import db_lab.data.DAOUtils;
import db_lab.data.Queries;
import db_lab.data.Tag;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record Material(int code, String description) {
    public class DAO {

        public static Map<Material, Float> forProduct(Connection connection, int productId) {
            var materials = new HashMap<Material, Float>();

            try (
                var statement = DAOUtils.prepare(connection, Queries.PRODUCT_COMPOSITION, productId);
                var resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    var code = resultSet.getInt("m.codMateriale");
                    var description = resultSet.getString("m.descrizione");
                    var composition = resultSet.getFloat("c.percentuale");
                    var material = new Material(code, description);
                    materials.put(material, composition);
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }

            return materials;
        }
    }
}
