#!/usr/bin/env python3
"""
生成微信小程序图标（TabBar 图标和页面图标）
"""
from PIL import Image, ImageDraw, ImageFont
import os
import math

# 创建 images 目录（如果不存在）
os.makedirs('images', exist_ok=True)

# TabBar 图标尺寸
TABBAR_SIZE = 81
# 页面图标尺寸
PAGE_ICON_SIZE = 40

# 颜色定义
COLOR_GRAY = (153, 153, 153, 255)  # 未选中状态
COLOR_DARK = (51, 51, 51, 255)     # 选中状态
COLOR_BLACK = (0, 0, 0, 255)       # 页面图标颜色

# ==================== TabBar 图标 ====================

def create_theater_icon(selected=False):
    """创建剧场图标（舞台/剧场场景）"""
    img = Image.new('RGBA', (TABBAR_SIZE, TABBAR_SIZE), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    
    color = COLOR_DARK if selected else COLOR_GRAY
    
    # 绘制舞台/剧场图标（简化版：舞台幕布）
    # 顶部幕布
    draw.ellipse([10, 5, TABBAR_SIZE-10, 25], fill=color)
    # 左侧幕布
    draw.polygon([(5, 5), (25, 5), (25, TABBAR_SIZE-5), (5, TABBAR_SIZE-5)], fill=color)
    # 右侧幕布
    draw.polygon([(TABBAR_SIZE-25, 5), (TABBAR_SIZE-5, 5), (TABBAR_SIZE-5, TABBAR_SIZE-5), (TABBAR_SIZE-25, TABBAR_SIZE-5)], fill=color)
    # 舞台
    draw.rectangle([25, TABBAR_SIZE-20, TABBAR_SIZE-25, TABBAR_SIZE-5], fill=color)
    
    return img

def create_profile_icon(selected=False):
    """创建我的/个人图标（用户头像）"""
    img = Image.new('RGBA', (TABBAR_SIZE, TABBAR_SIZE), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    
    color = COLOR_DARK if selected else COLOR_GRAY
    
    # 绘制用户头像图标
    # 头部（圆形）
    center_x, center_y = TABBAR_SIZE // 2, TABBAR_SIZE // 2 - 8
    head_radius = 12
    draw.ellipse([center_x - head_radius, center_y - head_radius,
                  center_x + head_radius, center_y + head_radius], fill=color)
    
    # 身体（上半身，类似倒梯形）
    body_top = center_y + head_radius
    body_bottom = TABBAR_SIZE - 10
    body_width_top = 20
    body_width_bottom = 28
    draw.polygon([
        (center_x - body_width_top // 2, body_top),
        (center_x + body_width_top // 2, body_top),
        (center_x + body_width_bottom // 2, body_bottom),
        (center_x - body_width_bottom // 2, body_bottom)
    ], fill=color)
    
    return img

# ==================== 页面图标 ====================

def create_search_icon():
    """创建搜索图标（放大镜）"""
    img = Image.new('RGBA', (PAGE_ICON_SIZE, PAGE_ICON_SIZE), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    
    center_x, center_y = PAGE_ICON_SIZE // 2, PAGE_ICON_SIZE // 2
    radius = 8
    handle_length = 6
    
    # 绘制圆形（放大镜主体）
    draw.ellipse([center_x - radius, center_y - radius - 2,
                  center_x + radius, center_y + radius - 2], 
                 outline=COLOR_BLACK, width=2)
    
    # 绘制手柄（右下角斜线）
    handle_start_x = center_x + radius - 2
    handle_start_y = center_y + radius - 4
    handle_end_x = handle_start_x + handle_length
    handle_end_y = handle_start_y + handle_length
    draw.line([(handle_start_x, handle_start_y), (handle_end_x, handle_end_y)], 
              fill=COLOR_BLACK, width=2)
    
    return img

def create_more_icon():
    """创建更多操作图标（三个点）"""
    img = Image.new('RGBA', (PAGE_ICON_SIZE, PAGE_ICON_SIZE), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    
    center_x, center_y = PAGE_ICON_SIZE // 2, PAGE_ICON_SIZE // 2
    dot_radius = 2
    spacing = 6
    
    # 绘制三个圆点（水平排列）
    for i in range(-1, 2):
        x = center_x + i * spacing
        draw.ellipse([x - dot_radius, center_y - dot_radius,
                      x + dot_radius, center_y + dot_radius], fill=COLOR_BLACK)
    
    return img

def create_settings_icon():
    """创建设置图标（齿轮）"""
    img = Image.new('RGBA', (PAGE_ICON_SIZE, PAGE_ICON_SIZE), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    
    center_x, center_y = PAGE_ICON_SIZE // 2, PAGE_ICON_SIZE // 2
    outer_radius = 12
    inner_radius = 6
    teeth_count = 8
    
    # 绘制齿轮外圈（带齿）
    for i in range(teeth_count):
        angle = 2 * math.pi * i / teeth_count
        # 外齿
        outer_x = center_x + outer_radius * math.cos(angle)
        outer_y = center_y + outer_radius * math.sin(angle)
        # 内齿
        inner_x = center_x + (outer_radius - 3) * math.cos(angle)
        inner_y = center_y + (outer_radius - 3) * math.sin(angle)
        
        if i % 2 == 0:
            # 外齿点
            draw.ellipse([outer_x - 2, outer_y - 2, outer_x + 2, outer_y + 2], fill=COLOR_BLACK)
            # 连接到内圈
            draw.line([(outer_x, outer_y), (inner_x, inner_y)], fill=COLOR_BLACK, width=2)
    
    # 绘制内圈
    draw.ellipse([center_x - inner_radius, center_y - inner_radius,
                  center_x + inner_radius, center_y + inner_radius], 
                 outline=COLOR_BLACK, width=2)
    
    return img

def create_id_icon():
    """创建 ID 图标（身份证/卡片）"""
    img = Image.new('RGBA', (PAGE_ICON_SIZE, PAGE_ICON_SIZE), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    
    # 绘制卡片外框
    card_width = 20
    card_height = 14
    card_x = (PAGE_ICON_SIZE - card_width) // 2
    card_y = (PAGE_ICON_SIZE - card_height) // 2
    
    draw.rectangle([card_x, card_y, card_x + card_width, card_y + card_height],
                   outline=COLOR_BLACK, width=2)
    
    # 绘制卡片上的线条（模拟文字）
    line_y1 = card_y + 4
    line_y2 = card_y + 8
    line_y3 = card_y + 12
    draw.line([(card_x + 2, line_y1), (card_x + card_width - 2, line_y1)], 
              fill=COLOR_BLACK, width=1)
    draw.line([(card_x + 2, line_y2), (card_x + card_width - 4, line_y2)], 
              fill=COLOR_BLACK, width=1)
    draw.line([(card_x + 2, line_y3), (card_x + card_width - 6, line_y3)], 
              fill=COLOR_BLACK, width=1)
    
    return img

def create_message_icon():
    """创建消息中心图标（对话气泡）"""
    img = Image.new('RGBA', (PAGE_ICON_SIZE, PAGE_ICON_SIZE), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    
    # 绘制对话气泡
    bubble_width = 16
    bubble_height = 12
    bubble_x = (PAGE_ICON_SIZE - bubble_width) // 2
    bubble_y = (PAGE_ICON_SIZE - bubble_height) // 2 - 2
    
    # 气泡主体（圆角矩形）
    draw.ellipse([bubble_x, bubble_y, bubble_x + bubble_width, bubble_y + bubble_height],
                 outline=COLOR_BLACK, width=2)
    
    # 气泡尾部（小三角形）
    tail_x = bubble_x + bubble_width - 4
    tail_y = bubble_y + bubble_height
    draw.polygon([
        (tail_x, tail_y),
        (tail_x + 4, tail_y),
        (tail_x + 2, tail_y + 4)
    ], fill=COLOR_BLACK)
    
    # 绘制消息提示点
    dot_x = bubble_x + bubble_width - 2
    dot_y = bubble_y + 2
    draw.ellipse([dot_x - 2, dot_y - 2, dot_x + 2, dot_y + 2], fill=COLOR_BLACK)
    
    return img

def create_history_icon():
    """创建观看历史图标（时钟）"""
    img = Image.new('RGBA', (PAGE_ICON_SIZE, PAGE_ICON_SIZE), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    
    center_x, center_y = PAGE_ICON_SIZE // 2, PAGE_ICON_SIZE // 2
    radius = 10
    
    # 绘制圆形表盘
    draw.ellipse([center_x - radius, center_y - radius,
                  center_x + radius, center_y + radius],
                 outline=COLOR_BLACK, width=2)
    
    # 绘制时针（指向左上）
    hour_length = 4
    hour_angle = -math.pi / 3  # -60度
    hour_end_x = center_x + hour_length * math.cos(hour_angle)
    hour_end_y = center_y + hour_length * math.sin(hour_angle)
    draw.line([(center_x, center_y), (hour_end_x, hour_end_y)],
              fill=COLOR_BLACK, width=2)
    
    # 绘制分针（指向12点）
    minute_length = 6
    minute_angle = -math.pi / 2  # -90度
    minute_end_x = center_x + minute_length * math.cos(minute_angle)
    minute_end_y = center_y + minute_length * math.sin(minute_angle)
    draw.line([(center_x, center_y), (minute_end_x, minute_end_y)],
              fill=COLOR_BLACK, width=2)
    
    # 绘制中心点
    draw.ellipse([center_x - 1, center_y - 1, center_x + 1, center_y + 1],
                 fill=COLOR_BLACK)
    
    return img

def create_service_icon():
    """创建客服图标（耳机）"""
    img = Image.new('RGBA', (PAGE_ICON_SIZE, PAGE_ICON_SIZE), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    
    center_x, center_y = PAGE_ICON_SIZE // 2, PAGE_ICON_SIZE // 2
    
    # 绘制左侧耳机
    left_x = center_x - 6
    draw.ellipse([left_x - 4, center_y - 4, left_x + 4, center_y + 4],
                 outline=COLOR_BLACK, width=2)
    
    # 绘制右侧耳机
    right_x = center_x + 6
    draw.ellipse([right_x - 4, center_y - 4, right_x + 4, center_y + 4],
                 outline=COLOR_BLACK, width=2)
    
    # 绘制连接线（头带）
    draw.arc([left_x - 2, center_y - 8, right_x + 2, center_y - 2],
             start=180, end=0, fill=COLOR_BLACK, width=2)
    
    return img

def create_help_icon():
    """创建帮助中心图标（问号）"""
    img = Image.new('RGBA', (PAGE_ICON_SIZE, PAGE_ICON_SIZE), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    
    center_x, center_y = PAGE_ICON_SIZE // 2, PAGE_ICON_SIZE // 2
    radius = 8
    
    # 绘制圆形外框
    draw.ellipse([center_x - radius, center_y - radius,
                  center_x + radius, center_y + radius],
                 outline=COLOR_BLACK, width=2)
    
    # 绘制问号
    # 上半部分（问号的上半圆和中间部分）
    draw.arc([center_x - 4, center_y - 6, center_x + 4, center_y + 2],
             start=0, end=180, fill=COLOR_BLACK, width=2)
    # 问号的竖线
    draw.line([(center_x, center_y + 2), (center_x, center_y + 4)],
              fill=COLOR_BLACK, width=2)
    # 问号的点
    draw.ellipse([center_x - 1, center_y + 5, center_x + 1, center_y + 7],
                 fill=COLOR_BLACK)
    
    return img

def create_feedback_icon():
    """创建意见反馈图标（编辑/铅笔）"""
    img = Image.new('RGBA', (PAGE_ICON_SIZE, PAGE_ICON_SIZE), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    
    # 绘制铅笔形状
    # 笔身（矩形）
    pen_x = PAGE_ICON_SIZE // 2 - 6
    pen_y = PAGE_ICON_SIZE // 2 - 8
    pen_width = 8
    pen_height = 12
    
    # 笔尖（三角形）
    tip_x = pen_x + pen_width // 2
    tip_y = pen_y + pen_height
    draw.polygon([
        (pen_x, pen_y + pen_height - 2),
        (pen_x + pen_width, pen_y + pen_height - 2),
        (tip_x, tip_y + 2)
    ], fill=COLOR_BLACK)
    
    # 笔身主体
    draw.rectangle([pen_x, pen_y, pen_x + pen_width, pen_y + pen_height - 2],
                   outline=COLOR_BLACK, width=2)
    
    # 笔尖高光
    draw.line([(tip_x - 1, tip_y), (tip_x + 1, tip_y)],
              fill=(255, 255, 255, 255), width=1)
    
    return img

def create_arrow_right_icon():
    """创建右箭头图标"""
    img = Image.new('RGBA', (PAGE_ICON_SIZE, PAGE_ICON_SIZE), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    
    center_x, center_y = PAGE_ICON_SIZE // 2, PAGE_ICON_SIZE // 2
    
    # 绘制箭头（向右）
    arrow_length = 8
    arrow_width = 4
    
    # 箭头主体（水平线）
    draw.line([(center_x - arrow_length // 2, center_y),
               (center_x + arrow_length // 2, center_y)],
              fill=COLOR_BLACK, width=2)
    
    # 箭头头部（三角形）
    arrow_head_x = center_x + arrow_length // 2
    draw.polygon([
        (arrow_head_x, center_y),
        (arrow_head_x - arrow_width, center_y - arrow_width // 2),
        (arrow_head_x - arrow_width, center_y + arrow_width // 2)
    ], fill=COLOR_BLACK)
    
    return img

# ==================== 生成所有图标 ====================

print("正在生成图标...\n")

# TabBar 图标
print("生成 TabBar 图标（81x81）...")
theater_img = create_theater_icon(selected=False)
theater_img.save('images/theater.png', 'PNG')
print("  ✓ 已生成 images/theater.png")

theater_active_img = create_theater_icon(selected=True)
theater_active_img.save('images/theater-active.png', 'PNG')
print("  ✓ 已生成 images/theater-active.png")

profile_img = create_profile_icon(selected=False)
profile_img.save('images/profile.png', 'PNG')
print("  ✓ 已生成 images/profile.png")

profile_active_img = create_profile_icon(selected=True)
profile_active_img.save('images/profile-active.png', 'PNG')
print("  ✓ 已生成 images/profile-active.png")

# 页面图标
print("\n生成页面图标（40x40）...")
search_img = create_search_icon()
search_img.save('images/search.png', 'PNG')
print("  ✓ 已生成 images/search.png")

more_img = create_more_icon()
more_img.save('images/more.png', 'PNG')
print("  ✓ 已生成 images/more.png")

settings_img = create_settings_icon()
settings_img.save('images/settings.png', 'PNG')
print("  ✓ 已生成 images/settings.png")

id_img = create_id_icon()
id_img.save('images/id.png', 'PNG')
print("  ✓ 已生成 images/id.png")

message_img = create_message_icon()
message_img.save('images/message.png', 'PNG')
print("  ✓ 已生成 images/message.png")

history_img = create_history_icon()
history_img.save('images/history.png', 'PNG')
print("  ✓ 已生成 images/history.png")

service_img = create_service_icon()
service_img.save('images/service.png', 'PNG')
print("  ✓ 已生成 images/service.png")

help_img = create_help_icon()
help_img.save('images/help.png', 'PNG')
print("  ✓ 已生成 images/help.png")

feedback_img = create_feedback_icon()
feedback_img.save('images/feedback.png', 'PNG')
print("  ✓ 已生成 images/feedback.png")

arrow_right_img = create_arrow_right_icon()
arrow_right_img.save('images/arrow-right.png', 'PNG')
print("  ✓ 已生成 images/arrow-right.png")

print("\n所有图标已生成完成！共生成 14 个图标文件。")
