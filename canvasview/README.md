CanvasView

涂鸦的View 可以在上面涂鸦

#### 实现的功能
*画笔
*橡皮
*左右旋转
*导出文件或者bitmap


*******

##### 使用

```java

    <FrameLayout
            android:layout_width="0dp"
            android:layout_height="0dp"
            android:background="@color/c_2b2b2b"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintBottom_toBottomOf="parent">
            <com.cyy.canvasview.CanvasView
                android:id="@+id/canvasView"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                />
    </FrameLayout>


```

#### 实现原理