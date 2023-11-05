import { ADD_CATEGORY, CHANGE_STATUS_CATEGORY, GET_ALL_CATEGORY, UPDATE_CATEGORY } from '../../redux/api/service/categoryService';
import { Button, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField, Tooltip } from '@mui/material';
import React, { useEffect, useState } from 'react'
import { TIME_OUT, debouncing } from '../../utils/Deboucing';
import { useDispatch, useSelector } from 'react-redux';
import { validateBlank, validateExistsCategoryName, validateExistsCategoryNameUpdate } from '../../utils/ValidationForm';

import CategoryIcon from '@mui/icons-material/Category';
import { DATA_CATEGORY } from '../../redux/selectors/selectors';
import EditIcon from '@mui/icons-material/Edit';
import FileUploadIcon from '@mui/icons-material/FileUpload';
import FormCategory from '../../components/form/FormCategory';
import FormEditCategory from '../../components/form/FormEditCategory';
import LockOpenOutlinedIcon from '@mui/icons-material/LockOpenOutlined';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import ProductByCategory from '../../components/modal/ProductByCategory';
import SettingsIcon from '@mui/icons-material/Settings';
import { formatText } from '../../utils/FormatText';

function Category() {
  const categories = useSelector(DATA_CATEGORY);
  const dispatch = useDispatch();

  const [toggleAdd, setToggleAdd] = useState(false)
  const [result, setResult] = useState({ categoryName: "", status: true })

  const [reload, setReload] = useState(false);
  const handleReload = () => setReload(!reload);

  const [open, setOpen] = useState(false);
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  const [openShow, setOpenShow] = useState(false);
  const handleOpenShow = () => setOpenShow(true);
  const handleCloseShow = () => setOpenShow(false);
  const [categoryId, setCategoryId] = useState(null);
  const handleShowProductByCategory = (id) => {
    setCategoryId(id);
    handleOpenShow();
  }

  const [edit, setEdit] = useState(null);
  const handleEditCatgory = (id) => {
    for (let i = 0; i < categories.length; i++) {
      if (categories[i].id === id) {
        setEdit(categories[i]);
        break;
      }
    }
    handleOpen();
    setErrorCategoryName('')
  }

  const [errorCategoryName, setErrorCategoryName] = useState('');

  const handleUpdateCategory = (e) => {
    e.preventDefault();
    // validate blank
    if (validateBlank(e.target.categoryName.value)) {
      setErrorCategoryName("Không được để trống");
      return;
    }
    // validate exists
    if (validateExistsCategoryNameUpdate(categories, e.target.categoryName.value, edit.categoryName)) {
      setErrorCategoryName("Tên thể loại đã bị trùng");
      return;
    }
    const result = {
      categoryName: formatText(e.target.categoryName.value),
      status: true,
    }
    dispatch(UPDATE_CATEGORY({ data: result, id: e.target.id.value }));
    handleReload();
    handleClose();
  }

  const handleCreateForm = () => {
    setToggleAdd(true);
    setErrorCategoryName('')
  }

  const handleChangeForm = (e) => {
    const { name, value } = e.target;
    setResult({ ...result, [name]: value });
  }

  const handleAddCategory = () => {
    // validate blank
    if (validateBlank(result.categoryName)) {
      setErrorCategoryName("Không được để trống");
      return;
    }
    // validate exists
    if (validateExistsCategoryName(categories, result.categoryName)) {
      setErrorCategoryName("CategoryName is exists");
      return;
    }
    dispatch(ADD_CATEGORY({ categoryName: formatText(result.categoryName), status: true }));
    setToggleAdd(false)
    setResult({ categoryName: "", status: true })
  }

  const handleChangeStatusShip = (id) => {
    dispatch(CHANGE_STATUS_CATEGORY(id));
    handleReload();
  }

  const handleCloseForm = () => {
    setErrorCategoryName("");
    setResult({ categoryName: "", status: true });
    setToggleAdd(false)
  }

  const [search, setSearch] = useState('');
  const handleChangeSearch = (e) => setSearch(e.target.value);

  useEffect(() => {
    dispatch(GET_ALL_CATEGORY(search));
  }, [reload, search])


  return (
    <div>
      <div className='flex justify-center text-3xl font-semibold uppercase'>quản lý thể loại sản phẩm</div>
      <div className='flex justify-between'>
        <div className="actions">
        </div>
        <div className="add_manager">
          <Button variant="contained" className='flex gap-2' onClick={handleCreateForm}> <CategoryIcon /> <span>Thêm Thể Loại</span> </Button>
        </div>
      </div>
      <div className="content  w-full mt-5 ">
        <div className="header bg-white p-6 shadow-md">
          <TextField onChange={debouncing(handleChangeSearch, TIME_OUT)} id="outlined-basic" size='small' fullWidth label="Tìm kiếm thẻ loại" name='search' variant="outlined" />
        </div>
        <div className="table w-full mt-5 shadow-md">
          <TableContainer component={Paper} >
            <Table sx={{ width: '100%' }} aria-label="simple table">
              <TableHead>
                <TableRow>
                  <TableCell align="center">STT</TableCell>
                  <TableCell align="center">TÊN THỂ LOẠI</TableCell>
                  <TableCell align='center'>TRẠNG THÁI</TableCell>
                  <TableCell align='center'>HÀNH ĐỘNG</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {toggleAdd && <FormCategory
                  result={result}
                  handleCloseForm={handleCloseForm}
                  handleChangeForm={handleChangeForm}
                  handleAddCategory={handleAddCategory}
                  errorCategoryName={errorCategoryName}
                />}
                {categories && categories.map((item, index) => {
                  return (
                    <TableRow
                      key={item?.id}
                      sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                    >
                      <TableCell align='center'>{index + 1}</TableCell>
                      <TableCell align='center'>
                        <p className='text-blue-700 uppercase underline hover:cursor-pointer' onClick={() => handleShowProductByCategory(item.id)}>{item?.categoryName}</p>
                      </TableCell>
                      <TableCell align='center'>{item?.status ? <i className="fa-solid fa-lock-open"></i> : <i className="fa-solid fa-lock"></i>}</TableCell>
                      <TableCell align='center'>
                        {item?.status ?
                          <div className='flex gap-2 justify-center'>
                            <Button onClick={() => handleEditCatgory(item?.id)} variant="contained" color='warning'>
                              <Tooltip title='edit'><EditIcon /></Tooltip>
                            </Button>
                            <Button onClick={() => handleChangeStatusShip(item?.id)} variant="contained" color='error'>
                              <Tooltip title='lock'><LockOutlinedIcon /></Tooltip>
                            </Button>
                          </div>
                          :
                          <Button onClick={() => handleChangeStatusShip(item?.id)} variant="contained" color='success'>
                            <Tooltip title='unlock'><LockOpenOutlinedIcon /></Tooltip>
                          </Button>
                        }
                      </TableCell>
                    </TableRow>
                  )
                })}
              </TableBody>
            </Table>
          </TableContainer>
        </div>
      </div>
      {
        edit && <FormEditCategory
          open={open}
          handleClose={handleClose}
          edit={edit}
          handleUpdateCategory={handleUpdateCategory}
          errorCategoryName={errorCategoryName}
        />
      }
      {
        openShow && <ProductByCategory
          openShow={openShow}
          handleCloseShow={handleCloseShow}
          categoryId={categoryId}
        />
      }
    </div >
  )
}

export default Category