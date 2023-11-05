import { ACCEPT_PRODUCT, DELETE_PRODUCT, GET_ALL_PRODUCT_BY_STORAGE_ID, UNDELETE_PRODUCT } from '../../redux/api/service/productService';
import { Button, Chip, CircularProgress, FormControl, InputLabel, MenuItem, Paper, Select, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField, Tooltip } from '@mui/material'
import { DATA_BILL_REMOTE, DATA_CATEGORY, DATA_PRODUCT } from '../../redux/selectors/selectors';
import React, { useEffect, useState } from 'react'
import { TIME_OUT, debouncing } from '../../utils/Deboucing';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom';
import { validateProductInBill, validateProductInInventory } from '../../utils/ValidateProduct';

import CheckIcon from '@mui/icons-material/Check';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import FileUploadIcon from '@mui/icons-material/FileUpload';
import FormEditProduct from '../../components/form/FormEditProduct';
import FormProduct from '../../components/form/FormProduct';
import { GET_ALL_BILL } from '../../redux/api/service/billService';
import { GET_ALL_CATEGORY } from '../../redux/api/service/categoryService';
import LoadingComponent from '../../components/loading/LoadingComponent';
import LockOpenOutlinedIcon from '@mui/icons-material/LockOpenOutlined';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import QuestionMarkIcon from '@mui/icons-material/QuestionMark';
import SettingsIcon from '@mui/icons-material/Settings';
import WidgetsIcon from '@mui/icons-material/Widgets';
import { exportExcel } from '../../utils/ExportExcel';

function ProductInStorage() {

  const [loading, setLoading] = useState(true);

  const { id, storageName } = useParams();
  const navigate = useNavigate();
  const products = useSelector(DATA_PRODUCT);
  const categories = useSelector(DATA_CATEGORY);
  const dispatch = useDispatch();

  // start reload
  const [reload, setReload] = useState(false);
  const handleReload = () => {
    setReload(!reload);
  }
  // end reload

  // start add product
  const [open, setOpen] = useState(false);
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);
  // end add product

  // start edit product
  const [openEdit, setOpenEdit] = useState(false);
  const handleOpenEdit = () => setOpenEdit(true);
  const handleCloseEdit = () => setOpenEdit(false);

  const [edit, setEdit] = useState(null);
  const handleEdit = (id) => {
    console.log(id)
    for (let i = 0; i < products.length; i++) {
      if (products[i].id === id) {
        setEdit(products[i]);
        break;
      }
    }
    handleReload();
    handleOpenEdit();
  }
  // end edit product

  // start handle status
  const handleAccept = (id) => {
    dispatch(ACCEPT_PRODUCT(id));
    setTimeout(() => {
      handleReload();
    }, 100)
  }

  const handleDelete = (id) => {
    dispatch(DELETE_PRODUCT(id));
    setTimeout(() => {
      handleReload();
    }, 100)
  }

  const handleUnDelete = (id) => {
    dispatch(UNDELETE_PRODUCT(id));
    setTimeout(() => {
      handleReload();
    }, 100)
  }
  // end handle status

  const [searchProduct, setSearchProduct] = useState("");
  const handleChangeSearchProduct = (e) => setSearchProduct(e.target.value);

  const [category, setCategory] = useState('ALL');
  const handleChange = (event) => (setCategory(event.target.value));

  const handleExportExcel = () => {
    exportExcel(`/api/v1/products/export/${id}`, `products_in_storage_${id}`)
  }

  useEffect(() => {
    dispatch(GET_ALL_PRODUCT_BY_STORAGE_ID({ search: searchProduct, filter: category, id: id }));
    dispatch(GET_ALL_CATEGORY(''));
    if (products || categories) {
      setTimeout(() => {
        setLoading(false);
      }, 800)
    }
  }, [reload, category, searchProduct])

  return (
    <div>
      <div className='flex justify-center text-3xl font-semibold uppercase'>quản lý sản phẩm trong kho : {storageName}</div>
      <div className='flex justify-between'>
        <div className="actions flex items-center gap-2">
          <Button variant="contained" onClick={() => navigate("/admin/product")}>Quay Lại</Button>
          <Button variant="contained" className='flex gap-2' onClick={handleExportExcel}> <FileUploadIcon /> <span>Xuất File Excel</span> </Button>
        </div>
        <div className="add_manager">
          <Button variant="contained" className='flex gap-2' onClick={handleOpen} > <WidgetsIcon /> <span>Thêm Sản Phẩm</span> </Button>
          <FormProduct
            open={open}
            handleClose={handleClose}
            handleReload={handleReload}
            storageId={id}
            setLoading={() => setLoading(true)}
          />
          {edit && <FormEditProduct
            openEdit={openEdit}
            handleCloseEdit={handleCloseEdit}
            handleReload={handleReload}
            edit={edit}
            storageId={id}
            products={products}
          />}
        </div>
      </div>
      <div className="content w-full mt-5">
        <div className="header bg-white p-6 shadow-md flex gap-2">
          <FormControl sx={{ width: "20%" }} size='small'>
            <InputLabel id="demo-simple-select-label">Lọc</InputLabel>
            <Select
              labelId="demo-simple-select-label"
              id="demo-simple-select"
              value={category}
              label="Filter"
              onChange={handleChange}
            >
              <MenuItem value={'ALL'}>TẤT CẢ</MenuItem>
              {categories && categories.map((item) => (<MenuItem value={item.categoryName}>{item.categoryName.toUpperCase()}</MenuItem>))}
            </Select>
          </FormControl>
          <TextField id="outlined-basic" size='small' onChange={debouncing(handleChangeSearchProduct, TIME_OUT)} fullWidth label="Tìm kiếm theo tên hoặc mã sản phẩm" name='search' variant="outlined" />
        </div>
        <div className="table w-full mt-5">
          <TableContainer component={Paper} >
            <Table sx={{ width: '100%' }} aria-label="simple table">
              <TableHead>
                <TableRow>
                  <TableCell align="center">STT</TableCell>
                  <TableCell align='left'>MÃ</TableCell>
                  <TableCell align="center">TÊN</TableCell>
                  <TableCell align='center'>NGÀY SẢN XUẤT</TableCell>
                  <TableCell align='center'>NGÀY HẾT HẠN</TableCell>
                  <TableCell align='center'>GIÁ THÀNH</TableCell>
                  <TableCell align='center'>SỐ LƯỢNG</TableCell>
                  <TableCell align='center'>CÂN NẶNG</TableCell>
                  <TableCell align='center'>THỂ LOẠI</TableCell>
                  <TableCell align='center'>TRẠNG THÁI</TableCell>
                  <TableCell align='center'>HÀNH ĐỘNG</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {!loading && products && products.map((e, index) => {
                  return (
                    <TableRow
                      key={e?.id}
                      sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                    >
                      <TableCell align='center'>{index + 1}</TableCell>
                      <TableCell align='left'>{e?.code}</TableCell>
                      <TableCell align='center'>{e?.productName} </TableCell>
                      <TableCell align='center'>{e?.createdDate} </TableCell>
                      <TableCell align='center'>{e?.dueDate}</TableCell>
                      <TableCell align='center'>{e?.price.toLocaleString() + " đ"}</TableCell>
                      <TableCell align='center'>{e?.quantity}</TableCell>
                      <TableCell align='center'>{e?.weight}</TableCell>
                      <TableCell align='center'>{e?.category.categoryName}</TableCell>
                      <TableCell align='center'>
                        {e?.statusName === "PENDING" ? <Chip label="CHỜ XÁC NHẬN" color="warning" /> : ''}
                        {e?.statusName === "ACCEPT" ? <Chip label="XÁC NHẬN" color="success" /> : ''}
                        {e?.statusName === "DELETE" ? <Chip label="TẠM KHÓA" color="error" /> : ''}
                      </TableCell>
                      <TableCell align='center'>
                        {e?.statusName === "PENDING" ?
                          <div className='flex gap-2 justify-center'>
                            <Button onClick={() => handleAccept(e?.id)} variant="contained" color='warning'>
                              <Tooltip title='lock'><CheckIcon /></Tooltip>
                            </Button>
                            <Button onClick={() => handleDelete(e?.id)} variant="contained" color='error'>
                              <Tooltip title='lock'><DeleteIcon /></Tooltip>
                            </Button>
                          </div> : ""}
                        {e?.statusName === "ACCEPT" ?
                          <div className='flex gap-2 justify-center'>
                            <Button onClick={() => handleEdit(e.id)} variant="contained" color='warning'>
                              <Tooltip title='edit'><EditIcon /></Tooltip>
                            </Button>
                            <Button onClick={() => handleDelete(e?.id)} variant="contained" color='error'>
                              <Tooltip title='lock'><LockOutlinedIcon /></Tooltip>
                            </Button>
                          </div> : ""}
                        {e?.statusName === "DELETE" && e?.quantity > 0 ? <Button onClick={() => handleUnDelete(e?.id)} variant="contained" color='success'>
                          <Tooltip title='unlock'><LockOpenOutlinedIcon /></Tooltip>
                        </Button> : ''}
                        {e?.quantity === 0 && e?.statusName === "DELETE" ? <Tooltip title='Vùi lòng nhập thêm sản phẩm'><QuestionMarkIcon /></Tooltip> : ''}
                      </TableCell>
                    </TableRow>
                  )
                })}
              </TableBody>
            </Table>
          </TableContainer>
          {loading && <LoadingComponent />}
        </div>
      </div>
    </div>
  )
}

export default ProductInStorage