USE  YUHSI_Project_1 

--delete from fratc


GO
CREATE TABLE fratc(
	 city nvarchar(5) NOT NULL,
	 dist nvarchar (20) NOT NULL,
	 hospitalName   nvarchar (50) NOT NULL,
	 addr   nvarchar (100) NOT NULL,
	 telephoneNumber   nvarchar (20) NOT NULL
PRIMARY KEY 
(
	 city  ,
	 dist  ,
	 hospitalName  )
)
GO
