<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="edu.unsw.comp9321.*, java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Bibliographic Library | Home</title>
	<link href="http://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
	
    <link type="text/css" rel="stylesheet" href="css/materialize.min.css"  media="screen,projection"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
</head>

<body>
	<script type="text/javascript" src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
	<script type="text/javascript" src="js/materialize.min.js"></script>
	 <script>
	   $(document).ready(function() {
	      $('select').material_select();
	  });
	</script>
	
	<!-- Header -->
	<jsp:include page="navbar.jsp" />
	<div class="section no-pad-bot" id="index-banner">
    	<div class="container">
    		<br><br>
    		<h1 class="header center orange-text">Bibliographic Library</h1>
    		<br><br>
        </div>
 	</div>
 		
 		
 				
 	<!-- Basic Search Form -->
 	<div class="row">
 		<div class="col s8 offset-s2">
 			<div class="card white">
        		<div class="card-content black-text">
		 			<div class="card-title">
			 			<form action="setup" method="post">
							<div class="row">
								<div class="input-field col s6">
							    	<input placeholder="Search" name="searchQuery" type="text" class="validate">
							  	</div>
						  	  	<div class="input-field col s3">
				    				<select name="pubType">
								    	<option selected>Any</option>
							        	<option>Article</option>
								        <option>Inproceedings</option>
								        <option>Proceedings</option>
								        <option>Book</option>
								        <option>Incollection</option>
								        <option>Phdthesis</option>
								        <option>Masterthesis</option>
								        <option>WWW</option>
				    				</select>
				  				</div>
								<div class="input-field col s3" style="vertical-align: middle">
				  					<button class="btn waves-effect waves-light" type="submit" value="Search">Search
				  						<i class="material-icons right">send</i>
				  					</button>
				  					<input type="hidden" name="action" value="search"/>
				  				</div>
							</div>
						</form>
					</div>
					<div class="card-action">
						<form id="aSearchForm" action="setup" method="post">
							<div class="row">
				                <div class="col s3">
				                    <a href ="search.jsp" id="search">Advanced Search</a>
				    			</div>
				                
				                <div class="col s3">
				                    <a href ="cart.jsp">Shopping Cart</a>
				                </div>
			                </div>
				        </form>
					</div>
					
				</div>
			</div>
		</div>
 	</div>
	
	<!-- Random Bibliographies -->
	<div class="section no-pad-bot" id="index-banner">
    	<div class="container">
    		<br><br>
    		<h2 class="center orange-text">Bibliographies of Interest</h2>
    		<br><br>
        </div>
 	</div>

	<div class="container">
		<div class="row valign-wrapper">
			<div class="col s10 offset-s1">
     			<div class="card valign grey lighten-1" >
     				<p>There are ${fn:length(randomPubs)}</p>
			        <table class="left highlighted striped responsive-table">
			        	<thead>
							<tr>
								<th> Title </th>
								<th> Author</th>
								<th> Publication Type</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="pub" items="${randomPubs}">
								<tr>
									<td><c:out value="${pub.title}" /></td>
									<td><c:out value="${pub.formattedAuthors}" /></td>
									<td><c:out value="${pub.type}" /></td>
								</tr>
							</c:forEach>
						</tbody>	
		        	</table>
				</div>
  			</div>
		</div>
	</div>
    
    
    <jsp:include page="footer.jsp" />
</body>
</html>
