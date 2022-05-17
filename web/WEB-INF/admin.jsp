<%-- 
    Document   : admin
    Created on : 11-Nov-2021, 9:40:18 PM
    Author     : Alix
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="style.css">
        <title>Admin Page</title>
        <script type="text/javascript">
            function googleTranslateElementInit() {
                new google.translate.TranslateElement({pageLanguage: 'en'}, 'google_translate_element');
            }
        </script>
        <script type="text/javascript" src="//translate.google.com/translate_a/element.js?cb=googleTranslateElementInit"></script>
    </head>
    <body>
        <div class="container">
            <div class="sidenav">
                <h3>HOME nVentory</h3>
                <h4>Welcome, ${userObject.firstName} ${userObject.lastName}</h4>
                <ul>
                    <li><a href="admin">Admin</a></li>
                    <li><a href="inventory">Inventory</a></li>
                    <li><a href="ExportToExcel">Export To Excel</a></li>
                    <li><a href="login?logout">Logout</a></li>
                    <br>
                    <li><a>Language:</a></li>
                </ul>
                <div id="google_translate_element"></div>
            </div>
            <div class="contentPage">
                <h2>Admin</h2>
                <div class="AdminManageTable">
                    <h3>Manage Users</h3>
                    <ul>
                        <li>
                            <span>User Email</span>
                            <span>First Name</span>
                            <span>Last Name</span>
                            <span>Role</span>
                            <span>Active</span>
                            <span>Delete</span>
                            <span>Edit</span>
                        </li>
                        <c:forEach var="user" items="${userList}">
                            <li>
                                <span>${user.email}</span>
                                <span>${user.firstName}</span>
                                <span>${user.lastName}</span>
                                <span>${user.getRole().getRoleName()}</span>
                                <span>
                                    <input type="checkbox" value="${user.active}"
                                           <c:if test="${user.active == true}">checked</c:if> >                                          
                                    </span>
                                <c:if test="${SelectedUser == null}">
                                    <span>

                                        <form method="POST">
                                            <input type="submit" value="Delete">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="userPK" value="${user.email}">
                                        </form>
                                    </span>
                                </c:if>
                                <span>
                                    <form method="get">
                                        <input type="submit" value="Edit">
                                        <input type="hidden" name="action" value="edit">
                                        <input type="hidden" name="userPK" value="${user.email}">
                                    </form>
                                </span>
                            </li>
                        </c:forEach>
                    </ul>
                    <c:if test="${errorM_deleteAdminSelf}">
                        <p>Error. Admin users cannot delete themselves</p>
                    </c:if>
                </div>            
                <div class="manageTable">
                    <h3>Search Item</h3>
                    <div>
                        <form action="admin" method="GET">
                            <input type="text" name="searchItem" placeholder="Search by Item Name">
                            <input type="submit" value="Search">
                            <input type="hidden" name="action" value="search">
                        </form>
                    </div>
                </div>
                <c:if test="${searchItems != null}">

                    <div class="manageTable">
                        <h3>Search Result</h3>
                        <ul>
                            <li>
                                <span>Item Name</span>
                                <span>Owner</span>
                                <span>First Name</span>
                                <span>Last Name</span>
                            </li>
                            <c:forEach var="eachItem" items="${searchItems}">
                                <li>
                                    <span>${eachItem.itemName}</span> 
                                    <span>${eachItem.getOwner().email}</span>
                                    <span>${eachItem.getOwner().firstName}</span>
                                    <span>${eachItem.getOwner().lastName}</span>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>

                </c:if>
                <c:if test="${SelectedUser == null}">
                    <div class="manageTable">
                        <h3>Add User</h3>
                        <ul>
                            <form action="admin" method="POST" id="addTable">
                                <li><input type="text" name="email" value="${user_email}" placeholder="Email"></li>
                                <li><input type="text" name="password" value="${user_password}" placeholder="Password"></li>
                                <li><input type="text" name="firstname" value="${user_firstname}" placeholder="First Name"></li>
                                <li><input type="text" name="lastname" value="${user_lastname}" placeholder="Last Name"></li>
                                <li><label>Role: </label>
                                    <select name="role">
                                        <option value="1" 
                                                <c:if test="${user_role == 1}">selected</c:if>>system admin</option>
                                                <option value="2" 
                                                <c:if test="${user_role == 2}">selected</c:if>>regular user</option>
                                                <option value="3" 
                                                <c:if test="${user_role == 3}">selected</c:if>>company admin</option>
                                        </select>
                                    </li>
                                    <li>
                                        <label>Active</label>&nbsp;<input type="checkbox" name="active"
                                        <c:if test="${user_active}">
                                            checked
                                        </c:if>>  
                                </li>
                                <li>
                                    <input type="hidden" name="action" value="add">
                                    <input type="submit" value="Save">
                                </li>
                            </form>
                            <c:if test="${errorM_AddUser}">
                                <p>Error. Please fill all fields</p>
                            </c:if>
                        </ul>
                    </div>
                </c:if>
                <div class="manageTable">
                    <h3>Edit User</h3>
                    <ul>
                        <form action="admin" method="POST">
                            <li><input type="text" name="UpdateEmail" value="${SelectedUser.email}" placeholder="Email" readonly="true"></li>
                            <li><input type="text" name="UpdatePassword" value="${SelectedUser.password}" placeholder="Password"></li>
                            <li><input type="text" name="UpdateFirstname" value="${SelectedUser.firstName}" placeholder="First Name"></li>
                            <li><input type="text" name="UpdateLastname" value="${SelectedUser.lastName}" placeholder="Last Name"></li>
                            <li><label>Role: </label>
                                <select name="Updaterole">
                                    <option value="1" 
                                            <c:if test="${SelectedUser.getRole().getRoleId() == 1}">selected</c:if>>system admin</option>
                                            <option value="2" 
                                            <c:if test="${SelectedUser.getRole().getRoleId() == 2}">selected</c:if>>regular user</option>
                                            <option value="3" 
                                            <c:if test="${SelectedUser.getRole().getRoleId() == 3}">selected</c:if>>company admin</option>
                                    </select>
                                </li>
                                <li><label>Active</label>
                                    <input type="checkbox" name="UpdateActive" value="${SelectedUser.active}"
                                       <c:if test="${SelectedUser.active}">checked</c:if> >
                                </li>
                                <li>
                                    <input type="hidden" name="action" value="update">
                                    <input type="submit" value="Save">
                                </li>
                            </form>
                            <li>
                                <a href="admin">Cancel</a>
                            </li>
                        <c:if test="${errorM_EditUser}">
                            <p>Error. Please fill all fields</p>
                        </c:if>
                    </ul>
                </div>

                <div class="manageTable">
                    <h3>Manage Category</h3>
                    <div class="catTable">
                        <ul>
                            <li>
                                <span>Category Name</span>
                                <span>Edit Category</span>
                            </li>
                            <c:forEach var="cat" items="${categoryList}">
                                <li>
                                    <span>${cat.getCategoryName()}</span>
                                    <span>
                                        <form method="GET">
                                            <input type="submit" value="Edit">
                                            <input type="hidden" name="action" value="editCat">
                                            <input type="hidden" name="catPK" value="${cat.getCategoryId()}">
                                        </form>
                                    </span>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                    <div>
                        <h3>Edit Category</h3>
                        <form action="admin" method="POST">
                            <ul>
                                <li><input type="text" name="updateCatName" value="${SelectedCat.getCategoryName()}"></li> 
                                <li>
                                    <input type="hidden" name="action" value="updateCat">
                                    <input type="hidden" name="updateCatId" value="${SelectedCat.getCategoryId()}">
                                    <input type="submit" value="Save">
                                </li>
                            </ul>
                        </form>
                    </div>
                    <c:if test="${SelectedCat == null}">
                        <div>
                            <h3>Add Category</h3>
                            <form action="admin" method="POST">
                                <ul>
                                    <li><input type="text" name="addCateName" placeholer="Name of Category"></li>
                                    <li>
                                        <input type="hidden" name="action" value="addCat">
                                        <input type="submit" value="Save">
                                    </li>
                                </ul>
                            </form>
                        </div>
                    </c:if>

                </div>
            </div>
    </body>
</html>
