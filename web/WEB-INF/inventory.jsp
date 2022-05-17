<%-- 
    Document   : inventory
    Created on : 11-Nov-2021, 9:40:38 PM
    Author     : Alix
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link rel="stylesheet" type="text/css" href="style.css">
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Inventory Page</title>
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
                    <c:if test="${userObject.getRole().getRoleId() != 2}">
                        <li><a href="admin">Admin</a></li>
                        </c:if>
                    <li><a href="inventory">Inventory</a></li>
                        <c:if test="${userObject.getRole().getRoleId() == 2}">
                        <li><a href="inventory?manageAccount">Manage User Account</a></li>
                        </c:if>
                        <c:if test="${userObject.getRole().getRoleId() != 2}">
                        <li><a href="ExportToExcel">Export To Excel</a></li>
                        </c:if>
                    <li><a href="login?logout">Logout</a></li>
                    <br>
                    <li><a>Language:</a></li>
                </ul>
                <div id="google_translate_element"></div>
            </div>
            <div class="contentPage">
                <h2>Inventory</h2>
                <div class="inventoryTable">
                    <h3>Inventory List</h3>
                    <ul>
                        <li>
                            <span>Category</span>
                            <span>Name</span>
                            <span>Price</span>
                            <span>Delete</span>
                            <span>Edit</span>
                        </li>
                        <c:forEach var="item" items="${itemList}">
                            <li>
                                <span>${item.category.categoryName}</span>
                                <span>${item.itemName}</span>
                                <span>${item.price}</span> 
                                <c:if test="${selectedItem == null}">
                                    <span>
                                        <form method="POST">
                                            <input type="submit" value="Delete">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="itemPK" value="${item.itemId}">
                                        </form>
                                    </span>
                                </c:if>
                                <span>
                                    <form method="GET">
                                        <input type="submit" value="Edit">
                                        <input type="hidden" name="action" value="selectEdit">
                                        <input type="hidden" name="itemPK" value="${item.itemId}">
                                    </form>
                                </span>
                            </li>
                        </c:forEach>
                    </ul>
                    <c:if test="${errorM_deleteItem}">
                        <p>Please select correct delete item.</p>
                    </c:if>

                </div>
                <div class="manageTable">
                    <h3>Edit Item</h3>
                    <ul>
                        <form action="inventory" method="POST">
                            <label>Category: </label>
                            <select name="categoryid_edit">       

                                <c:forEach var="cat" items="${catList}">
                                    <li>
                                        <%-- display seleted item's cat in default, and avoid duplicate category name --%>
                                        <c:if test="${cat.categoryId == selectedItem.getCategory().categoryId}">
                                        <option value="${cat.categoryId}" selected>${cat.categoryName}</option>
                                    </c:if>
                                    <c:if test="${cat.categoryId != selectedItem.getCategory().categoryId}">
                                        <option value="${cat.categoryId}">${cat.categoryName}</option>
                                    </c:if>

                                    </li>
                                </c:forEach>                  

                            </select>

                            <li><input type="text" name="editItemName" value="${selectedItem.itemName}" placeholder="Name"></li>
                            <li><input type="number" name="editItemPrice" value="${selectedItem.price}" min="0" placeholder="Price"></li>
                            <li>
                                <input type="submit" value="Save">
                                <input type="hidden" name="editItemId" value="${selectedItem.itemId}">
                                <input type="hidden" name="action" value="edit">
                            </li>
                            <li>
                                <a href="inventory">Cancel</a>
                            </li>
                        </form>
                    </ul>
                    <c:if test="${errorM_editItem}">
                        <p>Please complete item name and item price.</p>
                    </c:if>
                </div>
                <c:if test="${selectedItem == null}">
                    <div class="manageTable">
                        <h3>Add Item</h3>
                        <ul>
                            <form action="inventory" method="POST">
                                <label>Category: </label>
                                <select name="categoryid">       

                                    <c:forEach var="cat" items="${catList}">
                                        <li>
                                        <option value="${cat.categoryId}">${cat.categoryName}</option>
                                        </li>
                                    </c:forEach>                  

                                </select>


                                <li><input type="text" name="itemname" value="${item_name}" placeholder="Name"></li>
                                <li><input type="number" name="itemprice" value="${item_price}" min="0" placeholder="Price"></li>
                                <li>
                                    <input type="submit" value="Save">
                                    <input type="hidden" name="action" value="add">
                                </li>
                            </form>
                        </ul>
                        <c:if test="${errorM_addItem}">
                            <p>Please complete item name and item price.</p>
                        </c:if>
                    </div>
                </c:if>
            </div>
        </div>
    </body>
</html>
