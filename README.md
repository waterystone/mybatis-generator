
使用方法如下：


       
    public static void main(String[] args) { // 
        String url = "jdbc:mysql://localhost:3306";
        String userName = "root";
        String password = "1234567890";
        
        GenerateService generateService = new GenerateService(url, userName, password);
        
        String dbName = "mybatis";
        String[] tables = {"hotel_book_info"};
        //生成代码的路径
        String filePath = "D:/test";
        generateService.generateCode(dbName, tables, filePath);
    }
    
