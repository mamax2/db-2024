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
import java.util.List;
import java.util.Map;

public final class Tag {

    public final String name;

    public Tag(String name) {
        this.name = name == null ? "" : name;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else return switch (other) {
            case Tag t -> t.name.equals(this.name);
            default -> false;
        };
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    public final class DAO {

        public static List<Tag> listForProduct(Connection connection, int productId) {
            var tags = new ArrayList<Tag>();

            try (
                // 1. Prepare the query you want to run
                var statement = DAOUtils.prepare(connection, Queries.TAGS_FOR_PRODUCT, productId);
                // 2. Run the query and get back a `ResultSet`
                var resultSet = statement.executeQuery();
            ) {
                // 3. Accumulate all the values of the result set into a list of tags
                //    iterating over it, row by row, until we run out of results.
                while (resultSet.next()) {
                    // 3-a. For each row we read the data we need and create a tag
                    //      out of it.
                    var tagName = resultSet.getString("e.nomeTag");
                    var tag = new Tag(tagName);
                    tags.add(tag);
                }
            } catch (Exception e) {
                // Note how we wrap all checked `SQLException`s in `DAOException`s so
                // that they do not bubble up all the function calls.
                throw new DAOException(e);
            }

            return tags;
        }
    }
}
