    public void findPotentialSiblings() {
        Long user1_id = 123L;
        String user1FirstName = "User1FirstName";
        String user1LastName = "User1LastName";
        Long user2_id = 456L;
        String user2FirstName = "User2FirstName";
        String user2LastName = "User2LastName";
        SiblingInfo s = new SiblingInfo(user1_id, user1FirstName, user1LastName, user2_id, user2FirstName, user2LastName);
        this.siblings.add(s);


        try (Statement stmt =
                     oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                             ResultSet.CONCUR_READ_ONLY)) {

            // For each month, find the number of users born that month
            // Sort them in descending order of count
            ResultSet rst = stmt.executeQuery("select U1.user_id,U1.first_name,U1.last_name, U2.user_id,U2.first_name,U2.last_name from tajik.public_users U1 "+
                            "join tajik.public_users U2 on U1.user_id<U2.user_id and U1.year_of_birth-U2.year_of_birth<10 and U2.year_of_birth-U1.year_of_birth<10 "+
                            "join tajik.public_friends F on (U1.user_id=F.user1_id and U2.user_id=F.user2_id) or (U1.user_id=F.user2_id and U2.user_id=F.user1_id) "+
                            "join tajik.public_user_hometown_city H1 on U1.user_id=H1.hometown_city_id "+
                            "join tajik.public_user_hometown_city H2 on U2.user_id=H2.hometown_city_id and H1.hometown_city_id=H2.hometown_city_id "+
                            "order by U1.user1_id asc, U2.user2_id asc ");

            while(rst.next()){
                s = new SiblingInfo(rst.getLong(1),rst.getString(2),rst.getString(3),rst.getLong(4),rst.getString(5),rst.getString(6));
                this.siblings.add(s);

            }

            



            // Close statement and result set
            rst.close();
            stmt.close();
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
    }